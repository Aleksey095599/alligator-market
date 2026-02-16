package com.alligator.market.backend.provider.maintenance.config.projection.db.passport.dao;

import com.alligator.market.backend.provider.maintenance.projection.db.passport.dao.ProviderPassportDbProjectionDaoAdapter;
import com.alligator.market.domain.provider.maintenance.projection.db.passport.dao.ProviderPassportDbProjectionDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Конфигурация wiring {@link ProviderPassportDbProjectionDao}.
 */
@Configuration(proxyBeanMethods = false)
public class ProviderPassportDbProjectionDaoConfig {

    public static final String BEAN_PROVIDER_PASSPORT_DB_PROJECTION_DAO = "providerPassportDbProjectionDao";

    /**
     * JDBC-адаптер доменного DAO для пакетных операций с паспортами провайдеров.
     */
    @Bean(BEAN_PROVIDER_PASSPORT_DB_PROJECTION_DAO)
    public ProviderPassportDbProjectionDao providerPassportDbProjectionDao(JdbcTemplate jdbcTemplate) {
        return new ProviderPassportDbProjectionDaoAdapter(jdbcTemplate);
    }
}
