package com.alligator.market.backend.sourcing.config.plan;

import com.alligator.market.backend.sourcing.config.plan.api.mapper.MarketDataSourcePlanApiMapperWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.command.create.CreateMarketDataSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.command.delete.DeleteMarketDataSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.command.replace.ReplaceMarketDataSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.query.existence.SourcePlanExistenceQueryWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.query.get.GetMarketDataSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.query.list.ListMarketDataSourcePlansServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.query.options.adapter.MarketDataSourcePlanOptionsQueryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Единая внешняя точка входа в wiring фичи sourcing.plan.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        CreateMarketDataSourcePlanServiceWiringConfig.class,
        DeleteMarketDataSourcePlanServiceWiringConfig.class,
        ReplaceMarketDataSourcePlanServiceWiringConfig.class,
        GetMarketDataSourcePlanServiceWiringConfig.class,
        ListMarketDataSourcePlansServiceWiringConfig.class,
        SourcePlanExistenceQueryWiringConfig.class,
        MarketDataSourcePlanOptionsQueryWiringConfig.class,
        MarketDataSourcePlanApiMapperWiringConfig.class
})
public class SourcingPlanFeatureWiringConfig {
}
