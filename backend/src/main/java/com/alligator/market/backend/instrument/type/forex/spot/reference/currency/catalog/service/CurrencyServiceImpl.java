package com.alligator.market.backend.instrument.type.forex.spot.reference.currency.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.service.CurrencyStorageService;
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

    private final CurrencyStorageService storageService;

    @Override
    public String createCurrency(Currency currency) {
        String code = storageService.add(currency);
        log.info("Currency {} saved", code);
        return code;
    }

    @Override
    public void updateCurrency(Currency currency) {
        storageService.update(currency);
        log.info("Currency {} updated", currency.code());
    }

    @Override
    public void deleteCurrency(String code) {
        storageService.remove(code);
        log.info("Currency {} deleted", code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Currency> findAll() {
        List<Currency> result = storageService.getAll();
        log.debug("Found {} currencies", result.size());
        return result;
    }
}
