package com.alligator.market.backend.provider.maintenance.config.orchestration.service;

import com.alligator.market.backend.provider.maintenance.properties.ProviderMaintenanceProperties;
import com.alligator.market.backend.provider.maintenance.orchestration.service.ProviderMaintenanceOrchestrator;
import com.alligator.market.backend.provider.maintenance.orchestration.task.ProviderMaintenanceTask;
import com.alligator.market.backend.provider.maintenance.config.orchestration.tasks.ProviderMaintenanceTasksConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * Конфигурация wiring {@link ProviderMaintenanceOrchestrator}.
 */
@Configuration(proxyBeanMethods = false)
@Import(ProviderMaintenanceTasksConfig.class)
@EnableConfigurationProperties(ProviderMaintenanceProperties.class)
public class ProviderMaintenanceOrchestrationConfig {

    /**
     * Бин оркестратора.
     */
    @Bean
    public ProviderMaintenanceOrchestrator providerMaintenanceOrchestrator(
            @Qualifier(ProviderMaintenanceTasksConfig.BEAN_TASKS)
            List<ProviderMaintenanceTask> tasks,
            ProviderMaintenanceProperties props
    ) {
        return new ProviderMaintenanceOrchestrator(tasks, props);
    }

}
