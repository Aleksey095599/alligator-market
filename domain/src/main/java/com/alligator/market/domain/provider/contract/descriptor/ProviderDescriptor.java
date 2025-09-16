package com.alligator.market.domain.provider.contract.descriptor;

import java.util.Objects;

/**
 * Иммутабельный набор статических атрибутов.
 *
 * @param providerCode           Технический код провайдера
 * @param displayName            Отображаемое имя провайдера (user friendly)
 * @param deliveryMode           Режим доставки рыночных данных: PULL или PUSH {@link AccessMethod}
 * @param accessMethod           Метод доступа к рыночным данным {@link AccessMethod}
 * @param bulkSubscription       Поддержка массовой подписки одним запросом
 */
public record ProviderDescriptor(
        String providerCode,
        String displayName,
        DeliveryMode deliveryMode,
        AccessMethod accessMethod,
        boolean bulkSubscription
) {
    public ProviderDescriptor {

        // ↓↓ Базовые проверки аргументов
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(accessMethod, "accessMethod must not be null");
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        if (providerCode.isBlank()) {
            throw new IllegalArgumentException("providerCode must not be blank");
        }
        if (displayName.isBlank()) {
            throw new IllegalArgumentException("displayName must not be blank");
        }
    }
}
