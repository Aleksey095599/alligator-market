package com.alligator.market.backend.provider.maintenance.config.orchestration;

import com.alligator.market.backend.provider.maintenance.orchestration.bootstrap.ProviderMaintenanceBootstrap;
import com.alligator.market.backend.provider.maintenance.orchestration.properties.ProviderMaintenanceProperties;
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
 * Конфигурация wiring оркестрации provider maintenance.
 */
@Configuration(proxyBeanMethods = false)
@Import(ProviderMaintenanceTasksConfig.class)
@EnableConfigurationProperties(ProviderMaintenanceProperties.class)
public class ProviderMaintenanceOrchestrationConfig {

    /**
     * Бин оркестратора задач обслуживания провайдеров.
     */
    @Bean
    public ProviderMaintenanceOrchestrator providerMaintenanceOrchestrator(
            @Qualifier(ProviderMaintenanceTasksConfig.BEAN_TASKS)
            List<ProviderMaintenanceTask> tasks,
            ProviderMaintenanceProperties props
    ) {
        return new ProviderMaintenanceOrchestrator(tasks, props);
    }

    /**
     * Бин bootstrap-процесса обслуживания при старте приложения.
     */
    @Bean
    public ProviderMaintenanceBootstrap providerMaintenanceBootstrap(
            ProviderMaintenanceOrchestrator orchestrator,
            ProviderMaintenanceProperties props
    ) {
        return new ProviderMaintenanceBootstrap(orchestrator, props);
    }
}
