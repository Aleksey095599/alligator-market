package com.alligator.market.domain.provider.model.passport;

/**
 * Режимы доставки данных провайдерами рыночных данных.
 */
public enum DeliveryMode {
    PUSH, // <-- Провайдер активно отправляет данные при их появлении
    PULL  // <-- Клиент периодически запрашивает актуальные данные
}
