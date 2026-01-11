package com.alligator.market.domain.provider.contract.passport;

import java.util.Objects;

/**
 * Паспорт провайдера: иммутабельные паспортные метаданные провайдера (витрина).
 *
 * <p>Назначение: отображение в каталоге, UI/API, логах и диагностике.</p>
 *
 * <p>Важный момент: Паспорт не задаёт бизнес-логику поведения провайдера или связанных бизнес-процессов.
 * Ошибки в паспорте рассматриваются как ошибка метаданных и исправляются без изменения бизнес-логики.</p>
 *
 * @param displayName      Отображаемое имя провайдера (user friendly)
 * @param deliveryMode     Режим доставки рыночных данных: PULL или PUSH {@link DeliveryMode}
 * @param accessMethod     Метод доступа к рыночным данным {@link AccessMethod}
 * @param bulkSubscription Поддержка массовой подписки одним запросом
 */
public record ProviderPassport(
        String displayName,
        DeliveryMode deliveryMode,
        AccessMethod accessMethod,
        boolean bulkSubscription
) {
    public ProviderPassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(accessMethod, "accessMethod must not be null");

        if (displayName.isBlank()) {
            throw new IllegalArgumentException("displayName must not be blank");
        }
    }
}
