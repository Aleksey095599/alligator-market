package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.command.update;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.exception.CurrencyNotFoundException;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * Use-case сервис обновления валюты.
 */
@Slf4j
public final class UpdateCurrencyService {

    private final CurrencyRepository currencyRepository;

    public UpdateCurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = Objects.requireNonNull(currencyRepository,
                "currencyRepository must not be null");
    }

    public void update(Currency currency) {
        Objects.requireNonNull(currency, "currency must not be null");

        Currency current = currencyRepository.findByCode(currency.code())
                .orElseThrow(() -> new CurrencyNotFoundException(currency.code()));

        // Если изменений нет -> пропускаем обновление без записи в хранилище.
        if (current.equals(currency)) {
            log.debug("Currency {} update skipped: nothing to change", currency.code().value());
            return;
        }

        Currency updated = currencyRepository.update(currency);
        log.info("Currency {} updated", updated.code().value());
    }
}
