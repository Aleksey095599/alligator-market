package com.alligator.market.backend.provider.maintenance.config.orchestration.bootstrap;

import com.alligator.market.backend.provider.maintenance.orchestration.bootstrap.ProviderMaintenanceBootstrap;
import com.alligator.market.backend.provider.maintenance.orchestration.properties.ProviderMaintenanceProperties;
import com.alligator.market.backend.provider.maintenance.orchestration.service.ProviderMaintenanceOrchestrator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация bootstrap-процесса обслуживания провайдеров.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderMaintenanceBootstrapConfig {

    /**
     * Бин bootstrap-процесса.
     */
    @Bean
    public ProviderMaintenanceBootstrap providerMaintenanceBootstrap(
            ProviderMaintenanceOrchestrator orchestrator,
            ProviderMaintenanceProperties props
    ) {
        return new ProviderMaintenanceBootstrap(orchestrator, props);
    }
}
