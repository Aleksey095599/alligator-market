package com.alligator.market.backend.provider.maintenance.orchestration.report;

import java.util.List;

/**
 * Отчёт по выполнению задач обслуживания.
 *
 * @param results список результатов (в порядке выполнения)
 */
public record ProviderMaintenanceReport(
        List<ProviderMaintenanceTaskResult> results
) {
    public boolean hasFailures() {
        return results.stream().anyMatch(r -> r.status() == ProviderMaintenanceTaskStatus.FAILED);
    }

    public List<ProviderMaintenanceTaskResult> failed() {
        return results.stream().filter(r -> r.status() == ProviderMaintenanceTaskStatus.FAILED).toList();
    }

    public long successCount() {
        return results.stream().filter(r -> r.status() == ProviderMaintenanceTaskStatus.SUCCESS).count();
    }

    public long failedCount() {
        return results.stream().filter(r -> r.status() == ProviderMaintenanceTaskStatus.FAILED).count();
    }

    public long skippedCount() {
        return results.stream().filter(r -> r.status() == ProviderMaintenanceTaskStatus.SKIPPED).count();
    }
}
