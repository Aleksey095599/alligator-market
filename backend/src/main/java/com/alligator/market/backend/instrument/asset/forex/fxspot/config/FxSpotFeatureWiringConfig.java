package com.alligator.market.backend.instrument.asset.forex.fxspot.config;

import com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.command.create.CreateFxSpotServiceWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.command.delete.DeleteFxSpotServiceWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.command.update.UpdateFxSpotServiceWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.query.list.ListFxSpotsServiceWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.query.reference.currency.FxSpotCurrencyReferenceQueryWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.usage.contributor.FxSpotUsageContributorWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Единая внешняя точка входа в wiring фичи instrument.asset.forex.fxspot.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        CreateFxSpotServiceWiringConfig.class,
        UpdateFxSpotServiceWiringConfig.class,
        DeleteFxSpotServiceWiringConfig.class,
        ListFxSpotsServiceWiringConfig.class,
        FxSpotCurrencyReferenceQueryWiringConfig.class,
        FxSpotUsageContributorWiringConfig.class
})
public class FxSpotFeatureWiringConfig {
}
