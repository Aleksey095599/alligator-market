package com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.contributor;

import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.contributor.CurrencyUsageContributor;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.contributor.fxspot.FxSpotCurrencyUsageContributor;
import com.alligator.market.domain.instrument.asset.forex.spot.repository.FxSpotRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация wiring contributor для проверки использования валюты.
 */
@Configuration(proxyBeanMethods = false)
public class CurrencyUsageContributorWiringConfig {

    public static final String BEAN_FX_SPOT_CURRENCY_USAGE_CONTRIBUTOR = "fxSpotCurrencyUsageContributor";

    /**
     * Contributor проверки использования валюты для инструментов FX Spot.
     */
    @Bean(BEAN_FX_SPOT_CURRENCY_USAGE_CONTRIBUTOR)
    public CurrencyUsageContributor fxSpotCurrencyUsageContributor(FxSpotRepository fxSpotRepository) {
        return new FxSpotCurrencyUsageContributor(fxSpotRepository);
    }
}
