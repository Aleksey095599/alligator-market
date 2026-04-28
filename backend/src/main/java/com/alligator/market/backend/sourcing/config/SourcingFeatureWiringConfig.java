package com.alligator.market.backend.sourcing.config;

import com.alligator.market.backend.sourcing.config.plan.api.mapper.InstrumentSourcePlanApiMapperWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.command.create.CreateInstrumentSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.command.delete.DeleteInstrumentSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.command.replace.ReplaceInstrumentSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.query.existence.SourcePlanExistenceQueryWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.query.get.GetInstrumentSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.query.list.ListInstrumentSourcePlansServiceWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Единая внешняя точка входа в wiring фичи sourcing.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        InstrumentSourcePlanApiMapperWiringConfig.class,
        GetInstrumentSourcePlanServiceWiringConfig.class,
        ListInstrumentSourcePlansServiceWiringConfig.class,
        CreateInstrumentSourcePlanServiceWiringConfig.class,
        ReplaceInstrumentSourcePlanServiceWiringConfig.class,
        DeleteInstrumentSourcePlanServiceWiringConfig.class,
        SourcePlanExistenceQueryWiringConfig.class
})
public class SourcingFeatureWiringConfig {

    public static final String BEAN_SOURCE_PLAN_EXISTENCE_QUERY_PORT =
            SourcePlanExistenceQueryWiringConfig.BEAN_SOURCE_PLAN_EXISTENCE_QUERY_PORT;
}
