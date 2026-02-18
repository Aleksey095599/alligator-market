package com.alligator.market.backend.provider.maintenance.config.orchestration.tasks;

import com.alligator.market.domain.provider.maintenance.task.ProviderMaintenanceTask;
import com.alligator.market.backend.provider.maintenance.config.projection.db.passport.task.ProviderPassportDbProjectionTaskConfig;
import com.alligator.market.backend.provider.maintenance.projection.db.passport.task.ProviderPassportDbProjectionTask;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * Конфигурация wiring единого набора ВСЕХ задач по обслуживанию провайдеров.
 */
@Configuration(proxyBeanMethods = false)
@Import(ProviderPassportDbProjectionTaskConfig.class)
public class ProviderMaintenanceTasksConfig {

    public static final String BEAN_TASKS = "providerMaintenanceTasks";

    /**
     * Единый упорядоченный набор задач provider maintenance.
     */
    @Bean(BEAN_TASKS)
    public List<ProviderMaintenanceTask> providerMaintenanceTasks(
            @Qualifier(ProviderPassportDbProjectionTaskConfig.BEAN_PROVIDER_PASSPORT_DB_PROJECTION_TASK)
            ProviderPassportDbProjectionTask providerPassportDbProjectionTask
    ) {
        return List.of(providerPassportDbProjectionTask);
    }
}
