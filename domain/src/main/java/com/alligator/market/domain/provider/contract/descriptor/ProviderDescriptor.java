package com.alligator.market.domain.provider.contract.descriptor;

import java.util.Locale;
import java.util.Objects;

/**
 * Дескриптор провайдера: иммутабельный набор статических атрибутов.
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
        // ↓↓ null-проверки аргументов
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(accessMethod, "accessMethod must not be null");

        // Убираем пробелы по краям
        providerCode = providerCode.trim();
        displayName = displayName.trim();

        // ↓↓ Базовая валидация аргументов
        if (providerCode.isBlank()) {
            throw new IllegalArgumentException("providerCode must not be blank");
        }
        if (providerCode.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("providerCode must not contain whitespaces");
        }
        if (!providerCode.equals(providerCode.toUpperCase(Locale.ROOT))) {
            throw new IllegalArgumentException("providerCode must be upper case");
        }
        if (displayName.isBlank()) {
            throw new IllegalArgumentException("displayName must not be blank");
        }
    }
}
