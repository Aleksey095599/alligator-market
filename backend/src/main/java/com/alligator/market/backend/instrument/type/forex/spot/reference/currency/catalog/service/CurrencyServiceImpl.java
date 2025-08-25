package com.alligator.market.backend.instrument.type.forex.spot.reference.currency.catalog.service;

import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.reference.currency.service.CurrencyDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация контракта сервиса {@link CurrencyService}.
 * Применяем доменный сервис, реализующий бизнес логику работы с репозиторием валют.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyDomainService domain;

    @Override
    public String createCurrency(Currency currency) {
        String code = domain.create(currency);
        log.info("Currency {} created", code);
        return code;
    }

    @Override
    public void updateCurrency(Currency currency) {
        domain.update(currency);
        log.info("Currency {} updated", currency.code());
    }

    @Override
    public void deleteCurrency(String code) {
        domain.remove(code);
        log.info("Currency {} deleted", code);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Currency> findAll() {
        List<Currency> result = domain.getAll();
        log.debug("Found {} currencies", result.size());
        return result;
    }
}
