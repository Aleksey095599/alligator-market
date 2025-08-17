package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.service;

import com.alligator.market.domain.instrument.type.forex.outright.catalog.FxOutrightStorage;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.catalog.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.catalog.exception.CurrencyUsedInPairsException;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.catalog.exception.DuplicateCurrencyException;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.catalog.CurrencyStorage;
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
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyStorage storage;
    private final FxOutrightStorage fxOutrightStorage;

    @Override
    public String createCurrency(Currency currency) {
        storage.findByCode(currency.code()).ifPresent(c -> {
            throw new DuplicateCurrencyException("code", currency.code());
        });
        storage.findByName(currency.name()).ifPresent(c -> {
            throw new DuplicateCurrencyException("name", currency.name());
        });
        String code = storage.save(currency);
        log.info("Currency {} saved", code);
        return code;
    }

    @Override
    public void updateCurrency(Currency currency) {
        storage.findByCode(currency.code())
                .orElseThrow(() -> new CurrencyNotFoundException(currency.code()));

        storage.findByName(currency.name()).ifPresent(c -> {
            if (!c.code().equals(currency.code())) {
                throw new DuplicateCurrencyException("name", currency.name());
            }
        });
        storage.save(currency);
        log.info("Currency {} updated", currency.code());
    }

    @Override
    public void deleteCurrency(String code) {
        storage.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));

        // Проверка, что валюта не используется в инструментах FX_OUTRIGHT
        if (fxOutrightStorage.existsByCurrency(code)) {
            throw new CurrencyUsedInPairsException(code);
        }

        storage.deleteByCode(code);
        log.info("Currency {} deleted", code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Currency> findAll() {
        List<Currency> result = storage.findAll();

        log.debug("Found {} currencies", result.size());
        return result;
    }
}
