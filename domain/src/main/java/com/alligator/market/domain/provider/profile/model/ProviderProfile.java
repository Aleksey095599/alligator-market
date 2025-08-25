package com.alligator.market.domain.provider.profile.model;

import com.alligator.market.domain.instrument.type.InstrumentType;

import java.util.Set;

/**
 * Модель профиля провайдера, содержащего информацию о провайдере рыночных данных.
 *
 * @param providerProfileStatus   Статус профиля {@link ProviderProfileStatus}
 * @param providerCode            Технический код провайдера
 * @param displayName             Отображаемое имя провайдера (user friendly)
 * @param instrumentsSupported    Поддерживаемые инструменты {@link InstrumentType}
 * @param deliveryMode            Режим доставки рыночных данных: PULL или PUSH {@link ProviderAccessMethod}
 * @param accessMethod            Метод доступа к рыночным данным {@link ProviderAccessMethod}
 * @param bulkSubscription        Поддержка массовой подписки одним запросом
 * @param minPollMs               Минимально допустимый интервал опроса в миллисекундах
 */
public record ProviderProfile(

        ProviderProfileStatus providerProfileStatus,
        String providerCode,
        String displayName,
        Set<InstrumentType> instrumentsSupported,
        ProviderDeliveryMode deliveryMode,
        ProviderAccessMethod accessMethod,
        boolean bulkSubscription,
        int minPollMs
) {}
