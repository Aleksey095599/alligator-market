package com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.usage.contributor;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor.FxSpotUsageContributor;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor.sourcing.SourcePlanFxSpotUsageContributor;
import com.alligator.market.backend.sourceplan.config.plan.application.query.existence.SourcePlanExistenceQueryWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.query.existence.port.SourcePlanExistenceQueryPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link FxSpotUsageContributor}.
 */
@Configuration(proxyBeanMethods = false)
@Import(SourcePlanExistenceQueryWiringConfig.class)
public class FxSpotUsageContributorWiringConfig {

    public static final String BEAN_SOURCE_PLAN_FX_SPOT_USAGE_CONTRIBUTOR = "sourcePlanFxSpotUsageContributor";

    @Bean(BEAN_SOURCE_PLAN_FX_SPOT_USAGE_CONTRIBUTOR)
    public FxSpotUsageContributor sourcePlanFxSpotUsageContributor(
            @Qualifier(SourcePlanExistenceQueryWiringConfig.BEAN_SOURCE_PLAN_EXISTENCE_QUERY_PORT)
            SourcePlanExistenceQueryPort sourcePlanExistenceQueryPort
    ) {
        return new SourcePlanFxSpotUsageContributor(sourcePlanExistenceQueryPort);
    }
}
