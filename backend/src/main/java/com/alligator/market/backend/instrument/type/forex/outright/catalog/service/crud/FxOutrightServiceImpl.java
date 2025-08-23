package com.alligator.market.backend.instrument.type.forex.outright.catalog.service.crud;

import com.alligator.market.domain.instrument.type.forex.outright.contract.FxOutrightStorage;
import com.alligator.market.domain.instrument.type.forex.outright.exception.FxOutrightDuplicateException;
import com.alligator.market.domain.instrument.type.forex.outright.exception.FxOutrightNotFoundException;
import com.alligator.market.domain.instrument.type.forex.outright.exception.FxOutrightSameCurrenciesException;
import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;
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

    private final FxOutrightStorage storage;

    @Override
    public String create(FxOutright instrument) {
        if (instrument.baseCurrency().equals(instrument.quoteCurrency())) {
            throw new FxOutrightSameCurrenciesException();
        }
        storage.find(instrument.getCode()).ifPresent(i -> {
            throw new FxOutrightDuplicateException(instrument.getCode());
        });
        storage.save(instrument);
        log.info("FxOutright {} saved", instrument.getCode());
        return instrument.getCode();
    }

    @Override
    public void updateQuoteDecimal(String code, int quoteDecimal) {
        FxOutright current = storage.find(code)
                .orElseThrow(() -> new FxOutrightNotFoundException(code));
        FxOutright updated = new FxOutright(
                current.baseCurrency(),
                current.quoteCurrency(),
                current.valueDateCode(),
                quoteDecimal
        );
        storage.save(updated);
        log.info("FxOutright {} updated", code);
    }

    @Override
    public void delete(String code) {
        storage.find(code).orElseThrow(() -> new FxOutrightNotFoundException(code));
        storage.delete(code);
        log.info("FxOutright {} deleted", code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FxOutright> findAll() {
        List<FxOutright> result = storage.findAll();
        log.debug("Found {} FX_OUTRIGHT instruments", result.size());
        return result;
    }
}
