package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.query.list;

import com.alligator.market.domain.instrument.catalog.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.catalog.forex.reference.currency.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * Use-case сервис получения списка валют.
 */
@Slf4j
public final class CurrencyListService {

    private final CurrencyRepository currencyRepository;

    public CurrencyListService(CurrencyRepository currencyRepository) {
        this.currencyRepository = Objects.requireNonNull(currencyRepository,
                "currencyRepository must not be null");
    }

    public List<Currency> findAll() {

        List<Currency> result = currencyRepository.findAll();
        log.debug("Found {} currencies", result.size());

        return result;
    }
}
