package com.alligator.market.domain.provider.contract.descriptor;

import java.util.Objects;

/**
 * Дескриптор провайдера: иммутабельный набор статических атрибутов.
 *
 * @param deliveryMode           Режим доставки рыночных данных: PULL или PUSH {@link AccessMethod}
 * @param accessMethod           Метод доступа к рыночным данным {@link AccessMethod}
 * @param bulkSubscription       Поддержка массовой подписки одним запросом
 */
public record ProviderDescriptor(
        DeliveryMode deliveryMode,
        AccessMethod accessMethod,
        boolean bulkSubscription
) {
    public ProviderDescriptor {
        // ↓↓ Базовая валидация аргументов
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(accessMethod, "accessMethod must not be null");
    }
}
