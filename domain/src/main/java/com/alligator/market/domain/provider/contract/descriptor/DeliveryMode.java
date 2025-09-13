package com.alligator.market.domain.provider.contract.descriptor;

/**
 * Список возможных режимов доставки данных провайдерами рыночных данных.
 */
public enum DeliveryMode {

    // Провайдер активно отправляет данные при их появлении
    PUSH,

    // Клиент периодически запрашивает актуальные данные
    PULL
}
