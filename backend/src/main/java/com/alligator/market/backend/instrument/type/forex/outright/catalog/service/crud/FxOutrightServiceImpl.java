package com.alligator.market.backend.instrument.type.forex.outright.catalog.service.crud;

import com.alligator.market.domain.instrument.type.forex.spot.storage.SpotStorage;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxOutrightDuplicateException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxOutrightNotFoundException;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxOutrightSameCurrenciesException;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация контракта сервиса {@link FxOutrightService}.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FxOutrightServiceImpl implements FxOutrightService {

    private final SpotStorage storage;

    @Override
    public String create(FxSpot instrument) {
        if (instrument.baseCurrency().equals(instrument.quoteCurrency())) {
            throw new FxOutrightSameCurrenciesException();
        }
        storage.find(instrument.getCode()).ifPresent(i -> {
            throw new FxOutrightDuplicateException(instrument.getCode());
        });
        storage.save(instrument);
        log.info("FxSpot {} saved", instrument.getCode());
        return instrument.getCode();
    }

    @Override
    public void updateQuoteDecimal(String code, int quoteDecimal) {
        FxSpot current = storage.find(code)
                .orElseThrow(() -> new FxOutrightNotFoundException(code));
        FxSpot updated = new FxSpot(
                current.baseCurrency(),
                current.quoteCurrency(),
                current.valueDateCode(),
                quoteDecimal
        );
        storage.save(updated);
        log.info("FxSpot {} updated", code);
    }

    @Override
    public void delete(String code) {
        storage.find(code).orElseThrow(() -> new FxOutrightNotFoundException(code));
        storage.delete(code);
        log.info("FxSpot {} deleted", code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxSpot> findAll() {
        List<FxSpot> result = storage.findAll();
        log.debug("Found {} FX_OUTRIGHT instruments", result.size());
        return result;
    }
}
