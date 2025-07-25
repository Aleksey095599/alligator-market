package com.alligator.market.domain.provider.profile;

/**
 * Список возможных методов доступа к рыночным данным провайдера.
 */
public enum AccessMethod {

    // Метод периодического опроса API провайдера для получения рыночных данных
    API_POLL,

    // Метод получения данных через WebSocket соединение в режиме реального времени
    WEBSOCKET,

    // Метод доступа через FIX протокол для высокочастотной торговли
    FIX_PROTOCOL
}
