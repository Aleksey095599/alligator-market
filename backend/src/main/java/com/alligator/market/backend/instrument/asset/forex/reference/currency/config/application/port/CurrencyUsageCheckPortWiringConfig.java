package com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.port;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor.CurrencyUsageContributor;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.port.CurrencyUsageCheckPort;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.port.adapter.CompositeCurrencyUsageCheckAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Wiring-конфигурация {@link CurrencyUsageCheckPort}.
 */
@Configuration(proxyBeanMethods = false)
public class CurrencyUsageCheckPortWiringConfig {

    public static final String BEAN_CURRENCY_USAGE_CHECK_PORT = "currencyUsageCheckPort";

    /**
     * Порт общей проверки использования валюты через набор contributor.
     */
    @Bean(BEAN_CURRENCY_USAGE_CHECK_PORT)
    public CurrencyUsageCheckPort currencyUsageCheckPort(List<CurrencyUsageContributor> contributors) {
        return new CompositeCurrencyUsageCheckAdapter(contributors);
    }
}
