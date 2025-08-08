package com.alligator.market.domain.provider.profile.model;

import com.alligator.market.domain.instrument.model.InstrumentType;

import java.util.Set;

/**
 * Профиль провайдера рыночных данных.
 *
 * @param providerCode             Технический код
 * @param displayName              Отображаемое имя
 * @param instrumentTypes          Поддерживаемые инструменты
 * @param deliveryMode             Режим доставки рыночных данных: PULL или PUSH
 * @param accessMethod             Метод доступа к рыночным данным: API_POLL, WEBSOCKET, FIX или другие
 * @param supportsBulkSubscription Поддержка массовой подписки одним запросом
 * @param minPollPeriodMs          Минимально допустимый интервал опроса в миллисекундах
 */
public record ProviderProfile(

        String providerCode,
        String displayName,
        Set<InstrumentType> instrumentTypes,
        DeliveryMode deliveryMode,
        AccessMethod accessMethod,
        boolean supportsBulkSubscription,
        int minPollPeriodMs
) {}
