package com.alligator.market.backend.instrument.asset.forex.reference.currency.config.application.usage.contributor;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.reference.currency.port.FxSpotCurrencyReferenceQueryPort;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.query.reference.currency.FxSpotCurrencyReferenceQueryWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor.CurrencyUsageContributor;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.application.usage.contributor.fxspot.FxSpotCurrencyUsageContributor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link CurrencyUsageContributor}.
 */
@Configuration(proxyBeanMethods = false)
@Import(FxSpotCurrencyReferenceQueryWiringConfig.class)
public class CurrencyUsageContributorWiringConfig {

    public static final String BEAN_FX_SPOT_CURRENCY_USAGE_CONTRIBUTOR = "fxSpotCurrencyUsageContributor";

    @Bean(BEAN_FX_SPOT_CURRENCY_USAGE_CONTRIBUTOR)
    public CurrencyUsageContributor fxSpotCurrencyUsageContributor(
            @Qualifier(FxSpotCurrencyReferenceQueryWiringConfig.BEAN_FX_SPOT_CURRENCY_REFERENCE_QUERY_PORT)
            FxSpotCurrencyReferenceQueryPort fxSpotCurrencyReferenceQueryPort
    ) {
        return new FxSpotCurrencyUsageContributor(fxSpotCurrencyReferenceQueryPort);
    }
}
