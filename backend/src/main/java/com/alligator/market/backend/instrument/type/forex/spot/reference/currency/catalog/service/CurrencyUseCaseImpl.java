package com.alligator.market.backend.instrument.type.forex.spot.reference.currency.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.service.CurrencyCatalog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса {@link CurrencyUseCase}.
 * Делегирует выполнение операций доменному классу {@link CurrencyCatalog},
 * который содержит бизнес-логику и проверки.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CurrencyUseCaseImpl implements CurrencyUseCase {

    private final CurrencyCatalog domainCatalog;

    @Override
    public String createCurrency(Currency currency) {
        String code = domainCatalog.create(currency);
        log.info("Currency {} created", code);
        return code;
    }

    @Override
    public void updateCurrency(Currency currency) {
        domainCatalog.update(currency);
        log.info("Currency {} updated", currency.code());
    }

    @Override
    public void deleteCurrency(String code) {
        domainCatalog.delete(code);
        log.info("Currency {} deleted", code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Currency> getAll() {
        List<Currency> result = domainCatalog.getAll();
        log.debug("Found {} currencies", result.size());
        return result;
    }
}
