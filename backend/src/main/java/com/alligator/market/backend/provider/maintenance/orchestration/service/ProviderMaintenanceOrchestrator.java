package com.alligator.market.backend.provider.maintenance.orchestration.service;

import com.alligator.market.backend.provider.maintenance.orchestration.config.ProviderMaintenanceProps;
import com.alligator.market.backend.provider.maintenance.orchestration.report.ProviderMaintenanceReport;
import com.alligator.market.backend.provider.maintenance.orchestration.report.ProviderMaintenanceTaskResult;
import com.alligator.market.backend.provider.maintenance.orchestration.report.ProviderMaintenanceTaskStatus;
import com.alligator.market.backend.provider.maintenance.orchestration.task.ProviderMaintenanceTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Оркестратор задач обслуживания провайдеров.
 *
 * <p>Назначение: координировать запуск задач обслуживания провайдеров.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderMaintenanceOrchestrator {

    /* Набор задач обслуживания (порядок задается через @Order на задачах). */
    private final List<ProviderMaintenanceTask> tasks;

    /* Настройки maintenance. */
    private final ProviderMaintenanceProps props;

    /**
     * Запустить все задачи обслуживания и вернуть отчёт.
     *
     * <p>Важно: оркестратор не падает при первой ошибке, а пытается выполнить все задачи и вернуть отчёт.</p>
     */
    public ProviderMaintenanceReport runAll() {
        Set<String> seenCodes = new HashSet<>();
        for (ProviderMaintenanceTask task : tasks) {
            Objects.requireNonNull(task, "task must not be null");

            String code = Objects.requireNonNull(task.code(), "task code must not be null");
            if (code.isBlank()) {
                throw new IllegalStateException("Task code must not be blank");
            }
            if (!seenCodes.add(code)) {
                throw new IllegalStateException("Duplicate provider maintenance task code: '" + code + "'");
            }
        }

        List<ProviderMaintenanceTaskResult> results = new ArrayList<>(tasks.size());

        for (ProviderMaintenanceTask task : tasks) {
            final String code = Objects.requireNonNull(task.code(), "task code must not be null");

            final boolean enabled = props.isTaskEnabled(code, task.enabledByDefault());
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

            final long startedAt = System.nanoTime();
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
