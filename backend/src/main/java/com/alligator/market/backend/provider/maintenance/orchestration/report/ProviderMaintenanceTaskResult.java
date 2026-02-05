package com.alligator.market.backend.provider.maintenance.orchestration.report;

/**
 * Результат выполнения конкретной задачи обслуживания провайдеров.
 *
 * @param code   код задачи
 * @param status статус выполнения
 * @param tookMs длительность выполнения в миллисекундах
 * @param error  ошибка (если есть)
 */
public record ProviderMaintenanceTaskResult(
        String code,
        ProviderMaintenanceTaskStatus status,
        long tookMs,
        Throwable error
) {
}
