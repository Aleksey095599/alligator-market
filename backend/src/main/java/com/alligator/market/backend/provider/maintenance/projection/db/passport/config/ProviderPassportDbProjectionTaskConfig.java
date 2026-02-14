package com.alligator.market.backend.provider.maintenance.projection.db.passport.config;

import com.alligator.market.backend.provider.maintenance.projection.db.passport.task.ProviderPassportDbProjectionTask;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.service.ProviderPassportDbProjection;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Конфигурация wiring задачи обновления проекции паспортов провайдеров.
 */
@Configuration(proxyBeanMethods = false)
@Import(ProviderPassportDbProjectionConfig.class)
public class ProviderPassportDbProjectionTaskConfig {

    public static final String BEAN_PROVIDER_PASSPORT_DB_PROJECTION_TASK = "providerPassportDbProjectionTask";

    /**
     * Задача обслуживания, выполняющая refresh проекции паспортов провайдеров.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_DB_PROJECTION_TASK)
    public ProviderPassportDbProjectionTask providerPassportDbProjectionTask(
            @Qualifier(ProviderPassportDbProjectionConfig.BEAN_PROVIDER_PASSPORT_DB_PROJECTION)
            ProviderPassportDbProjection projection
    ) {
        return new ProviderPassportDbProjectionTask(projection);
    }
}
