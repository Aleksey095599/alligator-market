package com.alligator.market.domain.provider.contract.passport;

import java.util.Objects;

/**
 * Паспорт провайдера: иммутабельные паспортные метаданные провайдера (витрина).
 *
 * <p>Назначение: отображение в каталоге, UI/API, логах и диагностике.</p>
 *
 * <p>Важный момент: паспорт не определяет бизнес-логику поведения провайдера или связанных процессов;
 * ошибки в паспорте рассматриваются как ошибки метаданных и исправляются без изменения бизнес-логики.</p>
 *
 * @param displayName      Отображаемое имя провайдера (user friendly)
 * @param deliveryMode     Режим доставки рыночных данных
 * @param accessMethod     Метод доступа к рыночным данным
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
