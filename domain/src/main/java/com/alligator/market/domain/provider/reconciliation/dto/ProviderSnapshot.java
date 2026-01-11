package com.alligator.market.domain.provider.reconciliation.dto;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;

import java.util.Objects;

/**
 * Снимок провайдера: содержит информацию о провайдере рыночных данных.
 *
 * <p>Применяется для извлечения данных о провайдере из контекста приложения.</p>
 *
 * @param code       Код провайдера
 * @param descriptor Дескриптор провайдера
 * @param policy     Политика провайдера
 */
public record ProviderSnapshot(
        ProviderCode code,
        ProviderDescriptor descriptor,
        ProviderPolicy policy
) {
    /**
     * Конструктор с проверками.
     */
    public ProviderSnapshot {
        Objects.requireNonNull(code, "Provider code must not be null");
        Objects.requireNonNull(descriptor, "Provider descriptor must not be null");
        Objects.requireNonNull(policy, "Provider policy must not be null");
    }
}
