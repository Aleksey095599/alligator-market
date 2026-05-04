package com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.command.delete;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.command.delete.DeleteCurrencyService;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.port.CurrencyUsageCheckPort;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.usage.port.CurrencyUsageCheckPortWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.persistence.repository.adapter.CurrencyRepositoryWiringConfig;
import com.alligator.market.domain.instrument.catalog.forex.reference.currency.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link DeleteCurrencyService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        CurrencyRepositoryWiringConfig.class,
        CurrencyUsageCheckPortWiringConfig.class
})
public class DeleteCurrencyServiceWiringConfig {

    public static final String BEAN_DELETE_CURRENCY_SERVICE = "deleteCurrencyService";

    @Bean(BEAN_DELETE_CURRENCY_SERVICE)
    public DeleteCurrencyService deleteCurrencyService(
            @Qualifier(CurrencyRepositoryWiringConfig.BEAN_CURRENCY_REPOSITORY)
            CurrencyRepository currencyRepository,
            @Qualifier(CurrencyUsageCheckPortWiringConfig.BEAN_CURRENCY_USAGE_CHECK_PORT)
            CurrencyUsageCheckPort currencyUsageCheckPort
    ) {
        return new DeleteCurrencyService(currencyRepository, currencyUsageCheckPort);
    }
}
