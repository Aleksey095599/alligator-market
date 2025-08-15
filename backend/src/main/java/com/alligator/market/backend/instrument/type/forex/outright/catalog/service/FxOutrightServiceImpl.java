package com.alligator.market.backend.instrument.type.forex.outright.catalog.service;

import com.alligator.market.domain.instrument.type.forex.outright.catalog.FxOutrightStorage;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.exeption.DuplicateFxOutrightException;
import com.alligator.market.domain.instrument.type.forex.outright.catalog.exeption.FxOutrightNotFoundException;
import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса FX_OUTRIGHT.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FxOutrightServiceImpl implements FxOutrightService {

    private final FxOutrightStorage storage;

    @Override
    public String create(FxOutright instrument) {
        storage.find(instrument.code()).ifPresent(i -> {
            throw new DuplicateFxOutrightException(instrument.code());
        });
        storage.save(instrument);
        log.info("FxOutright {} saved", instrument.code());
        return instrument.code();
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
        log.debug("Found {} fx outrights", result.size());
        return result;
    }
}
