package com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.service.crud;

import com.alligator.market.domain.instrument.type.forex.outright.contract.FxOutrightStorage;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.exception.CurrencyDuplicateException;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.exception.CurrencyUsedInFxOutrightException;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.outright.reference.currency.contract.CurrencyStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация контракта сервиса {@link CurrencyService}.
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
            throw new CurrencyDuplicateException("code", currency.code());
        });
        storage.findByName(currency.name()).ifPresent(c -> {
            throw new CurrencyDuplicateException("name", currency.name());
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
                throw new CurrencyDuplicateException("name", currency.name());
            }
        });
        storage.save(currency);
        log.info("Currency {} updated", currency.code());
    }

    @Override
    public void deleteCurrency(String code) {
        storage.findByCode(code)
                .orElseThrow(() -> new CurrencyNotFoundException(code));
        // Проверяем, что валюта не используется в инструментах FX_OUTRIGHT, иначе нельзя удалять
        if (fxOutrightStorage.existsByCurrency(code)) {
            throw new CurrencyUsedInFxOutrightException(code);
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
