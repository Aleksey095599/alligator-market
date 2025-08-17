package com.alligator.market.backend.instrument.type.forex.outright.catalog.service;

import com.alligator.market.domain.instrument.type.forex.outright.catalog.FxOutrightStorage;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.exception.DuplicateFxOutrightException;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.exception.FxOutrightNotFoundException;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.exception.SameCurrenciesException;
import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса.
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
            throw new SameCurrenciesException();
        }
        storage.find(instrument.code()).ifPresent(i -> {
            throw new DuplicateFxOutrightException(instrument.code());
        });
        storage.save(instrument);
        log.info("FxOutright {} saved", instrument.code());
        return instrument.code();
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
