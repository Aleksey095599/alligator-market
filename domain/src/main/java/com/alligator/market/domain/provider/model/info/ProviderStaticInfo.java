package com.alligator.market.domain.provider.model.info;

/**
 * Статическая информация о провайдере.
 *
 * @param providerCode           Технический код провайдера
 * @param displayName            Отображаемое имя провайдера (user friendly)
 * @param deliveryMode           Режим доставки рыночных данных: PULL или PUSH {@link AccessMethod}
 * @param accessMethod           Метод доступа к рыночным данным {@link AccessMethod}
 * @param bulkSubscription       Поддержка массовой подписки одним запросом
 */
public record ProviderStaticInfo(

        String providerCode,
        String displayName,
        DeliveryMode deliveryMode,
        AccessMethod accessMethod,
        boolean bulkSubscription
) {}
