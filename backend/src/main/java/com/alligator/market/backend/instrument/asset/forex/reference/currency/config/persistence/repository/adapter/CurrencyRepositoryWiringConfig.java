package com.alligator.market.backend.instrument.asset.forex.reference.currency.config.persistence.repository.adapter;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.persistence.jooq.repository.JooqCurrencyRepositoryAdapter;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link CurrencyRepository}.
 */
@Configuration(proxyBeanMethods = false)
public class CurrencyRepositoryWiringConfig {

    public static final String BEAN_CURRENCY_REPOSITORY = "currencyRepository";

    @Bean(BEAN_CURRENCY_REPOSITORY)
    public CurrencyRepository currencyRepository(DSLContext dsl) {
        return new JooqCurrencyRepositoryAdapter(dsl);
    }
}
