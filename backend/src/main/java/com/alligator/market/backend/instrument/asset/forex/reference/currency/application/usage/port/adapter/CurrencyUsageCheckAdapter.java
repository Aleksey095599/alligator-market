package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.port.adapter;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor.CurrencyUsageContributor;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.port.CurrencyUsageCheckPort;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.code.CurrencyCode;

import java.util.List;
import java.util.Objects;

/**
 * Адаптер проверки использования валюты по всем подключенным contributors.
 */
public final class CurrencyUsageCheckAdapter implements CurrencyUsageCheckPort {

    /* Набор contributors, участвующих в общей проверке использования валюты. */
    private final List<CurrencyUsageContributor> contributors;

    public CurrencyUsageCheckAdapter(List<CurrencyUsageContributor> contributors) {
        Objects.requireNonNull(contributors, "contributors must not be null");

        this.contributors = List.copyOf(contributors);

        for (CurrencyUsageContributor contributor : this.contributors) {
            Objects.requireNonNull(contributor, "contributor must not be null");
        }
    }

    @Override
    public boolean isUsed(CurrencyCode currencyCode) {
        Objects.requireNonNull(currencyCode, "currencyCode must not be null");

        for (CurrencyUsageContributor contributor : contributors) {
            if (contributor.isUsed(currencyCode)) {
                return true;
            }
        }

        return false;
    }
}
