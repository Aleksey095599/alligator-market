package com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.port.adapter;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.port.CurrencyUsageCheckPort;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.port.adapter.FxSpotCurrencyUsageCheckAdapter;
import com.alligator.market.domain.instrument.asset.forex.spot.repository.FxSpotRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация wiring {@link CurrencyUsageCheckPort}.
 */
@Configuration(proxyBeanMethods = false)
public class CurrencyUsageCheckPortWiringConfig {

    public static final String BEAN_CURRENCY_USAGE_CHECK_PORT = "currencyUsageCheckPort";

    /**
     * Порт проверки использования валюты в FX Spot инструментах.
     */
    @Bean(BEAN_CURRENCY_USAGE_CHECK_PORT)
    public CurrencyUsageCheckPort currencyUsageCheckPort(FxSpotRepository fxSpotRepository) {
        return new FxSpotCurrencyUsageCheckAdapter(fxSpotRepository);
    }
}
