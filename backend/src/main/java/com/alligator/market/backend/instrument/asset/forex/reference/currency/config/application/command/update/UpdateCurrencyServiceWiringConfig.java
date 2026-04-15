package com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.command.update;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.command.update.UpdateCurrencyService;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.persistence.repository.adapter.CurrencyRepositoryWiringConfig;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link UpdateCurrencyService}.
 */
@Configuration(proxyBeanMethods = false)
@Import(CurrencyRepositoryWiringConfig.class)
public class UpdateCurrencyServiceWiringConfig {

    public static final String BEAN_UPDATE_CURRENCY_SERVICE = "updateCurrencyService";

    @Bean(BEAN_UPDATE_CURRENCY_SERVICE)
    public UpdateCurrencyService updateCurrencyService(
            @Qualifier(CurrencyRepositoryWiringConfig.BEAN_CURRENCY_REPOSITORY)
            CurrencyRepository currencyRepository
    ) {
        return new UpdateCurrencyService(currencyRepository);
    }
}
