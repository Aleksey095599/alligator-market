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
    /* Максимальная длина отображаемого имени провайдера. */
    private static final int MAX_DISPLAY_NAME_LENGTH = 50;

    public ProviderPassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(accessMethod, "accessMethod must not be null");

        String normalizedDisplayName = displayName.strip();
        if (normalizedDisplayName.isEmpty()) {
            throw new IllegalArgumentException("displayName must not be blank");
        }
        if (normalizedDisplayName.length() > MAX_DISPLAY_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    "displayName length must be <= " + MAX_DISPLAY_NAME_LENGTH + ": " + normalizedDisplayName.length()
            );
        }

        displayName = normalizedDisplayName;
    }
}
