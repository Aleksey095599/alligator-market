package com.alligator.market.domain.provider.contract.descriptor;

/**
 * Список возможных режимов доставки данных провайдерами рыночных данных.
 */
public enum DeliveryMode {
    PUSH, // Провайдер активно отправляет данные при их появлении
    PULL  // Клиент периодически запрашивает актуальные данные
}
