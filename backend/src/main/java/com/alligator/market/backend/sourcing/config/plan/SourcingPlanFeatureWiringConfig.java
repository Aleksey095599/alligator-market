package com.alligator.market.backend.sourcing.config.plan;

import com.alligator.market.backend.sourcing.config.plan.api.mapper.InstrumentSourcePlanApiMapperWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.command.create.CreateInstrumentSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.command.delete.DeleteInstrumentSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.command.replace.ReplaceInstrumentSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.query.existence.SourcePlanExistenceQueryWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.query.get.GetInstrumentSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.query.list.ListInstrumentSourcePlansServiceWiringConfig;
import com.alligator.market.backend.sourcing.config.plan.application.query.options.adapter.InstrumentSourcePlanOptionsQueryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Единая внешняя точка входа в wiring фичи sourcing.plan.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        CreateInstrumentSourcePlanServiceWiringConfig.class,
        DeleteInstrumentSourcePlanServiceWiringConfig.class,
        ReplaceInstrumentSourcePlanServiceWiringConfig.class,
        GetInstrumentSourcePlanServiceWiringConfig.class,
        ListInstrumentSourcePlansServiceWiringConfig.class,
        SourcePlanExistenceQueryWiringConfig.class,
        InstrumentSourcePlanOptionsQueryWiringConfig.class,
        InstrumentSourcePlanApiMapperWiringConfig.class
})
public class SourcingPlanFeatureWiringConfig {
}
