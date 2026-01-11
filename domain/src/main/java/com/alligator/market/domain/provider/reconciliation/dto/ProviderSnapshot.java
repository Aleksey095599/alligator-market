package com.alligator.market.domain.provider.reconciliation.dto;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;

import java.util.Objects;

/**
 * Снимок провайдера: содержит информацию о провайдере рыночных данных.
 *
 * <p>Применяется для извлечения данных о провайдере из контекста приложения.</p>
 *
 * @param code     Код провайдера
 * @param passport Паспорт провайдера
 * @param policy   Политика провайдера
 */
public record ProviderSnapshot(
        ProviderCode code,
        ProviderPassport passport,
        ProviderPolicy policy
) {
    /**
     * Конструктор с проверками.
     */
    public ProviderSnapshot {
        Objects.requireNonNull(code, "Provider code must not be null");
        Objects.requireNonNull(passport, "Provider passport must not be null");
        Objects.requireNonNull(policy, "Provider policy must not be null");
    }
}
