package com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.command.create;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.command.create.CreateCurrencyService;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.persistence.repository.adapter.CurrencyRepositoryWiringConfig;
import com.alligator.market.domain.instrument.catalog.forex.reference.currency.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link CreateCurrencyService}.
 */
@Configuration(proxyBeanMethods = false)
@Import(CurrencyRepositoryWiringConfig.class)
public class CreateCurrencyServiceWiringConfig {

    public static final String BEAN_CREATE_CURRENCY_SERVICE = "createCurrencyService";

    @Bean(BEAN_CREATE_CURRENCY_SERVICE)
    public CreateCurrencyService createCurrencyService(
            @Qualifier(CurrencyRepositoryWiringConfig.BEAN_CURRENCY_REPOSITORY)
            CurrencyRepository currencyRepository
    ) {
        return new CreateCurrencyService(currencyRepository);
    }
}
