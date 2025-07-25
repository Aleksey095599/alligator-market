package com.alligator.market.domain.provider.profile;

/**
 * Список возможных режимов доставки рыночных данных провайдера.
 */
public enum DeliveryMode {

    // Провайдер активно отправляет данные при их появлении
    PUSH,

    // Клиент периодически запрашивает актуальные данные
    PULL
}
