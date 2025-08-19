package com.alligator.market.domain.provider.profile.model;

/**
 * Доменная модель профиля провайдера рыночных данных.
 *
 * @param providerCode       Технический код провайдера
 * @param displayName        Отображаемое имя провайдера (user friendly)
 * @param deliveryMode       Режим доставки рыночных данных: PULL или PUSH
 * @param accessMethod       Метод доступа к рыночным данным: API_POLL, WEBSOCKET, FIX или другие
 * @param bulkSubscription   Поддержка массовой подписки одним запросом
 * @param minPollMs          Минимально допустимый интервал опроса в миллисекундах
 */
public record ProviderProfile(

        String providerCode,
        String displayName,
        ProviderDeliveryMode deliveryMode,
        ProviderAccessMethod accessMethod,
        boolean bulkSubscription,
        int minPollMs
) {}
