package com.alligator.market.backend.provider.maintenance.orchestration.service;

import com.alligator.market.backend.provider.maintenance.orchestration.properties.ProviderMaintenanceProperties;
import com.alligator.market.backend.provider.maintenance.orchestration.report.ProviderMaintenanceReport;
import com.alligator.market.backend.provider.maintenance.orchestration.report.ProviderMaintenanceTaskResult;
import com.alligator.market.backend.provider.maintenance.orchestration.report.ProviderMaintenanceTaskStatus;
import com.alligator.market.backend.provider.maintenance.orchestration.task.ProviderMaintenanceTask;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Оркестратор задач обслуживания провайдеров.
 *
 * <p>Назначение: координировать запуск задач обслуживания провайдеров.</p>
 */
@Slf4j
public class ProviderMaintenanceOrchestrator {

    /* Набор задач обслуживания (порядок задается в wiring-конфиге). */
    private final List<ProviderMaintenanceTask> tasks;

    /* Настройки maintenance. */
    private final ProviderMaintenanceProperties props;

    /* Конструктор. */
    public ProviderMaintenanceOrchestrator(
            List<ProviderMaintenanceTask> tasks,
            ProviderMaintenanceProperties props
    ) {
        this.tasks = List.copyOf(Objects.requireNonNull(tasks, "tasks must not be null"));
        this.props = Objects.requireNonNull(props, "props must not be null");
    }

    /**
     * Запустить все задачи обслуживания и вернуть отчёт.
     *
     * <p>Важно: оркестратор не падает при первой ошибке, а пытается выполнить все задачи и вернуть отчёт.</p>
     */
    public ProviderMaintenanceReport runAll() {
        Map<String, ProviderMaintenanceTask> tasksByCode = new LinkedHashMap<>();
        for (ProviderMaintenanceTask task : tasks) {
            Objects.requireNonNull(task, "task must not be null");

            String code = Objects.requireNonNull(task.code(), "task code must not be null");
            if (code.isBlank()) {
                throw new IllegalStateException("Task code must not be blank");
            }

            ProviderMaintenanceTask prev = tasksByCode.putIfAbsent(code, task);
            if (prev != null) {
                throw new IllegalStateException("Duplicate provider maintenance task code: '" + code + "'");
            }
        }

        List<ProviderMaintenanceTaskResult> results = new ArrayList<>(tasksByCode.size());

        for (Map.Entry<String, ProviderMaintenanceTask> entry : tasksByCode.entrySet()) {
            String code = entry.getKey();
            ProviderMaintenanceTask task = entry.getValue();

            boolean enabled = props.isTaskEnabled(code, task.enabledByDefault());
            if (!enabled) {
                log.info("Provider maintenance task skipped (disabled by config): {}", code);

                results.add(new ProviderMaintenanceTaskResult(
                        code,
                        ProviderMaintenanceTaskStatus.SKIPPED,
                        0,
                        null
                ));
                continue;
            }

            long startedAt = System.nanoTime();
            log.info("Running provider maintenance task: {}", code);

            try {
                task.run();

                long tookMs = (System.nanoTime() - startedAt) / 1_000_000;
                log.info("Provider maintenance task completed: {} ({} ms)", code, tookMs);

                results.add(new ProviderMaintenanceTaskResult(
                        code,
                        ProviderMaintenanceTaskStatus.SUCCESS,
                        tookMs,
                        null
                ));
            } catch (Exception ex) {
                long tookMs = (System.nanoTime() - startedAt) / 1_000_000;
                log.error("Provider maintenance task failed: {} ({} ms)", code, tookMs, ex);

                results.add(new ProviderMaintenanceTaskResult(
                        code,
                        ProviderMaintenanceTaskStatus.FAILED,
                        tookMs,
                        ex
                ));
            }
        }

        return new ProviderMaintenanceReport(List.copyOf(results));
    }
}
