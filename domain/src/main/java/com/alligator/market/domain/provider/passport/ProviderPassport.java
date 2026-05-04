package com.alligator.market.domain.provider.passport;

import com.alligator.market.domain.provider.passport.classification.AccessMethod;
import com.alligator.market.domain.provider.passport.classification.DeliveryMode;
import com.alligator.market.domain.provider.passport.vo.ProviderDisplayName;

import java.util.Objects;

/**
 * Паспорт провайдера: иммутабельные паспортные метаданные провайдера (витрина).
 *
 * <p>Назначение: отображение в каталоге, UI/API, логах и диагностике.</p>
 *
 * <p>Важный момент: паспорт не определяет бизнес-логику поведения провайдера или связанных процессов;
 * ошибки в паспорте рассматриваются как ошибки метаданных и исправляются без изменения бизнес-логики.</p>
 *
 * @param displayName      Отображаемое имя провайдера
 * @param deliveryMode     Режим доставки рыночных данных
 * @param accessMethod     Метод доступа к рыночным данным
 * @param bulkSubscription Поддержка массовой подписки одним запросом
 */
public record ProviderPassport(
        ProviderDisplayName displayName,
        DeliveryMode deliveryMode,
        AccessMethod accessMethod,
        boolean bulkSubscription
) {

    public ProviderPassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        Objects.requireNonNull(accessMethod, "accessMethod must not be null");
    }
}
