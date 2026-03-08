package com.alligator.market.domain.marketdata.provider.model.passport;

/**
 * Методы доступа провайдеров к рыночным данным.
 */
public enum AccessMethod {
    API_POLL,     // <-- Метод периодического опроса API провайдера для получения рыночных данных
    WEBSOCKET,    // <-- Метод получения данных через WebSocket соединение в режиме реального времени
    FIX_PROTOCOL  // <-- Метод доступа через FIX протокол для высокочастотной торговли
}
