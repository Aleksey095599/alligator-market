package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.port.adapter;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.contributor.CurrencyUsageContributor;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.port.CurrencyUsageCheckPort;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.vo.CurrencyCode;

import java.util.List;
import java.util.Objects;

/**
 * Композитный адаптер общей проверки использования валюты по всем подключенным contributor.
 */
public final class CompositeCurrencyUsageCheckAdapter implements CurrencyUsageCheckPort {

    /* Набор contributor, участвующих в общей проверке использования валюты. */
    private final List<CurrencyUsageContributor> contributors;

    public CompositeCurrencyUsageCheckAdapter(List<CurrencyUsageContributor> contributors) {
        // Проверяем, что входной список задан.
        Objects.requireNonNull(contributors, "contributors must not be null");

        // Делаем безопасную неизменяемую копию.
        this.contributors = List.copyOf(contributors);

        // Проверяем, что каждый contributor присутствует.
        for (CurrencyUsageContributor contributor : this.contributors) {
            Objects.requireNonNull(contributor, "contributor must not be null");
        }
    }

    @Override
    public boolean isUsed(CurrencyCode currencyCode) {
        // Проверяем обязательный входной аргумент.
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        for (CurrencyUsageContributor contributor : contributors) {
            if (contributor.isUsed(currencyCode)) {
                return true;
            }
        }

        return false;
    }
}
