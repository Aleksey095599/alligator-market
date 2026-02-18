package com.alligator.market.domain.provider.maintenance.task;

/**
 * Конкретная задача по обслуживанию провайдеров.
 */
public interface ProviderMaintenanceTask {

    /**
     * Код задачи (для логов/диагностики).
     */
    String code();

    /**
     * Запуск задачи.
     */
    void run();

    /**
     * Запускать ли задачу, если не найдено явное указание (например, в настройках приложения).
     */
    default boolean enabledByDefault() {
        return true;
    }
}
