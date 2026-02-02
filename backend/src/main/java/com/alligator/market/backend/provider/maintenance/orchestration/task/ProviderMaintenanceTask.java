package com.alligator.market.backend.provider.maintenance.orchestration.task;

/**
 * Конкретная задача для обслуживания провайдеров.
 *
 * <p>Назначение: стандартизировать запуск процессов обслуживания провайдеров
 * и дать одну точку расширения для добавления новых процессов.</p>
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
}
