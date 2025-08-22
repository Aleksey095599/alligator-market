package com.alligator.market.domain.provider.profile.model;

import com.alligator.market.domain.provider.model.Provider;

/**
 * Доменная модель профиля провайдера, содержащего статические параметры провайдера рыночных данных.
 * Профиль встраивается в доменную модель провайдера {@link Provider}
 *
 * @param providerCode       Технический код провайдера
 * @param displayName        Отображаемое имя провайдера (user friendly)
 * @param deliveryMode       Режим доставки рыночных данных: PULL или PUSH {@link ProviderAccessMethod}
 * @param accessMethod       Метод доступа к рыночным данным {@link ProviderAccessMethod}
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
