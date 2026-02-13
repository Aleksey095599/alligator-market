package com.alligator.market.backend.config.time;

/**
 * Конфигурация источника времени для приложения.
 *
 * <p>Назначение: Используется для получения текущего времени без прямых вызовов {@link java.time.Instant#now()}.
 * Это повышает тестируемость: в тестах можно подменять {@link Clock} на фиксированный.</p>
 */
public class TimeConfig {
}
