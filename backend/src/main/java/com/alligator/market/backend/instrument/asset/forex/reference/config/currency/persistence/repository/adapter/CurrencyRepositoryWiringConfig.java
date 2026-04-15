package com.alligator.market.backend.instrument.asset.forex.reference.config.currency.persistence.repository.adapter;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.persistence.jpa.CurrencyJpaRepository;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.persistence.repository.adapter.CurrencyRepositoryAdapter;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Явный wiring доменного {@link CurrencyRepository} через текущий adapter.
 */
@Configuration(proxyBeanMethods = false)
public class CurrencyRepositoryWiringConfig {

    public static final String BEAN_CURRENCY_REPOSITORY = "currencyRepository";

    /**
     * Сборка доменного repository-port для currency feature.
     */
    @Bean(BEAN_CURRENCY_REPOSITORY)
    public CurrencyRepository currencyRepository(CurrencyJpaRepository jpaRepository) {
        // Передаем зависимости adapter'а в явной точке сборки.
        return new CurrencyRepositoryAdapter(jpaRepository);
    }
}
