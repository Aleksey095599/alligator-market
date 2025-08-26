package com.alligator.market.domain.provider.profile.model;

import com.alligator.market.domain.instrument.type.InstrumentType;

import java.util.Set;

/**
 * Модель профиля провайдера.
 * Содержит информацию о провайдере.
 *
 * @param profileStatus   Статус профиля {@link ProfileStatus}
 * @param providerCode            Технический код провайдера
 * @param displayName             Отображаемое имя провайдера (user friendly)
 * @param instrumentsSupported    Поддерживаемые инструменты {@link InstrumentType}
 * @param deliveryMode            Режим доставки рыночных данных: PULL или PUSH {@link AccessMethod}
 * @param accessMethod            Метод доступа к рыночным данным {@link AccessMethod}
 * @param bulkSubscription        Поддержка массовой подписки одним запросом
 * @param minPollMs               Минимально допустимый интервал опроса в миллисекундах
 */
public record Profile(

        ProfileStatus profileStatus,
        String providerCode,
        String displayName,
        Set<InstrumentType> instrumentsSupported,
        DeliveryMode deliveryMode,
        AccessMethod accessMethod,
        boolean bulkSubscription,
        int minPollMs
) {}
