package com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.query.list;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.query.list.ListCurrenciesService;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.persistence.repository.adapter.CurrencyRepositoryWiringConfig;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link ListCurrenciesService}.
 */
@Configuration(proxyBeanMethods = false)
@Import(CurrencyRepositoryWiringConfig.class)
public class ListCurrenciesServiceWiringConfig {

    public static final String BEAN_LIST_CURRENCIES_SERVICE = "listCurrenciesService";

    /**
     * Собирает query service для получения списка валют.
     */
    @Bean(BEAN_LIST_CURRENCIES_SERVICE)
    public ListCurrenciesService listCurrenciesService(
            @Qualifier(CurrencyRepositoryWiringConfig.BEAN_CURRENCY_REPOSITORY)
            CurrencyRepository currencyRepository
    ) {
        return new ListCurrenciesService(currencyRepository);
    }
}
