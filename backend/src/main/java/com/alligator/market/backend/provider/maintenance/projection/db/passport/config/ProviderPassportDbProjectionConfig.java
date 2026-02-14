package com.alligator.market.backend.provider.maintenance.projection.db.passport.config;

import com.alligator.market.backend.provider.maintenance.context.config.ProviderMaintenanceContextScannerConfig;
import com.alligator.market.domain.provider.maintenance.context.scanner.ProviderContextScanner;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.dao.ProviderPassportDbProjectionDao;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.service.ProviderPassportDbProjection;
import com.alligator.market.domain.provider.repository.passport.ProviderPassportRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Конфигурация wiring для доменного сервиса проекции паспортов провайдеров из контекста приложения в БД.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        ProviderMaintenanceContextScannerConfig.class,
        ProviderPassportDbProjectionDaoConfig.class
})
public class ProviderPassportDbProjectionConfig {

    public static final String BEAN_PROVIDER_PASSPORT_DB_PROJECTION = "providerPassportDbProjection";

    /**
     * Доменный use-case: обновление проекции паспортов провайдеров в БД по данным из контекста.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_DB_PROJECTION)
    public ProviderPassportDbProjection providerPassportDbProjection(
            @Qualifier(ProviderMaintenanceContextScannerConfig.BEAN_PROVIDER_CONTEXT_SCANNER)
            ProviderContextScanner scanner,
            ProviderPassportRepository repository,
            @Qualifier(ProviderPassportDbProjectionDaoConfig.BEAN_PROVIDER_PASSPORT_DB_PROJECTION_DAO)
            ProviderPassportDbProjectionDao projectionDao
    ) {
        return new ProviderPassportDbProjection(scanner, repository, projectionDao);
    }
}
