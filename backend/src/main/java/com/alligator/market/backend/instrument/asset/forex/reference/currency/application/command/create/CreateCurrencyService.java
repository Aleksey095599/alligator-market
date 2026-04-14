package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.command.create;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Use-case сервис для сценария создания валюты.
 */
@Slf4j
public final class CreateCurrencyService {

    private final CurrencyRepository currencyRepository;

    public CreateCurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = Objects.requireNonNull(currencyRepository,
                "currencyRepository must not be null");
    }

    public Currency create(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        Currency created = currencyRepository.create(currency);
        log.info("Currency {} created", created.code().value());

        return created;
    }
}
