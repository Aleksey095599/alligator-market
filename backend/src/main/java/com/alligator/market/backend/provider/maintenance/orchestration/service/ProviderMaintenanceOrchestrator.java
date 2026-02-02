package com.alligator.market.backend.provider.maintenance.orchestration.service;

import com.alligator.market.backend.provider.maintenance.orchestration.task.ProviderMaintenanceTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Оркестратор задач обслуживания провайдеров.
 *
 * <p>Назначение: координировать запуск задач обслуживания провайдеров
 * и обеспечивать последовательность выполнения.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderMaintenanceOrchestrator {

    /* Набор задач обслуживания (порядок задается через @Order на задачах). */
    private final List<ProviderMaintenanceTask> tasks;

    /**
     * Запустить все задачи обслуживания.
     */
    public void runAll() {
        for (ProviderMaintenanceTask task : tasks) {
            log.info("Running provider maintenance task: {}", task.code());
            task.run();
        }
    }
}
