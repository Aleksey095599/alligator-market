package com.alligator.market.backend.provider.maintenance.orchestration.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация orchestration-слоя maintenance.
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ProviderMaintenanceProperties.class)
public class ProviderMaintenanceOrchestrationConfig {
}
