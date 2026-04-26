package com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.usage.contributor;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.usage.port.FxSpotCurrencyUsageQueryPort;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.query.usage.FxSpotCurrencyUsageQueryWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor.CurrencyUsageContributor;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor.fxspot.FxSpotCurrencyUsageContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Wiring-конфигурация {@link CurrencyUsageContributor}.
 */
@Configuration(proxyBeanMethods = false)
@Import(FxSpotCurrencyUsageQueryWiringConfig.class)
public class CurrencyUsageContributorWiringConfig {

    public static final String BEAN_FX_SPOT_CURRENCY_USAGE_CONTRIBUTOR = "fxSpotCurrencyUsageContributor";

    @Bean(BEAN_FX_SPOT_CURRENCY_USAGE_CONTRIBUTOR)
    public CurrencyUsageContributor fxSpotCurrencyUsageContributor(
            @Qualifier(FxSpotCurrencyUsageQueryWiringConfig.BEAN_FX_SPOT_CURRENCY_USAGE_QUERY_PORT)
            FxSpotCurrencyUsageQueryPort fxSpotCurrencyUsageQueryPort
    ) {
        return new FxSpotCurrencyUsageContributor(fxSpotCurrencyUsageQueryPort);
    }
}
