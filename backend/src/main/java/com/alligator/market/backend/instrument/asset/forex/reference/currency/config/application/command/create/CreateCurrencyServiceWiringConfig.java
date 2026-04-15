package com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.command.create;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.command.create.CreateCurrencyService;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.persistence.repository.adapter.CurrencyRepositoryWiringConfig;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Явный wiring use-case сервиса создания валюты.
 */
@Configuration(proxyBeanMethods = false)
@Import(CurrencyRepositoryWiringConfig.class)
public class CreateCurrencyServiceWiringConfig {

    public static final String BEAN_CREATE_CURRENCY_SERVICE = "createCurrencyService";

    /**
     * Сборка CreateCurrencyService с доменным repository-port.
     */
    @Bean(BEAN_CREATE_CURRENCY_SERVICE)
    public CreateCurrencyService createCurrencyService(
            @Qualifier(CurrencyRepositoryWiringConfig.BEAN_CURRENCY_REPOSITORY)
            CurrencyRepository currencyRepository
    ) {
        // Создаем use-case сервис с единственной зависимостью.
        return new CreateCurrencyService(currencyRepository);
    }
}
