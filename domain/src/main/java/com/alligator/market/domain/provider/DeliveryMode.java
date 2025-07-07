package com.alligator.market.domain.provider;

/**
 * Список возможных режимов доставки рыночных данных провайдера.
 */
public enum DeliveryMode {

    // PUSH режим - провайдер активно отправляет данные при их появлении
    PUSH,

    // PULL режим - клиент периодически запрашивает актуальные данные
    PULL
}
