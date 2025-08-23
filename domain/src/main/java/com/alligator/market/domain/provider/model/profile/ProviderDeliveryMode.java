package com.alligator.market.domain.provider.model.profile;

/**
 * Список возможных режимов доставки данных провайдерами рыночных данных.
 */
public enum ProviderDeliveryMode {

    // Провайдер активно отправляет данные при их появлении
    PUSH,

    // Клиент периодически запрашивает актуальные данные
    PULL
}
