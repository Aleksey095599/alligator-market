package com.alligator.market.backend.instrument.asset.forex.reference.currency.application.port;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.model.vo.CurrencyCode;

/**
 * Локальный вклад feature/aggregate в общую проверку использования валюты.
 */
public interface CurrencyUsageContributor {

    /**
     * Проверяет, используется ли валюта в рамках конкретного contributor.
     */
    boolean isUsed(CurrencyCode currencyCode);
}
