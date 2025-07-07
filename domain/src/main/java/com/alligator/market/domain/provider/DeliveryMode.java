package com.alligator.market.domain.provider;

/**
 * Режим доставки рыночных данных от провайдера.
 */
public enum DeliveryMode {
    // PUSH режим - провайдер активно отправляет данные при их появлении
    PUSH,

    // PULL режим - клиент периодически запрашивает актуальные данные
    PULL
}
