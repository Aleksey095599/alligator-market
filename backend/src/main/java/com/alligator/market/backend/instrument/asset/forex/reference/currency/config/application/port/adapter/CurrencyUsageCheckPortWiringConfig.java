package com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.port.adapter;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.contributor.CurrencyUsageContributor;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.port.CurrencyUsageCheckPort;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.port.adapter.CompositeCurrencyUsageCheckAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация wiring {@link CurrencyUsageCheckPort}.
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
