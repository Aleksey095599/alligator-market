package com.alligator.market.backend.sourceplan.config.plan;

import com.alligator.market.backend.sourceplan.config.plan.api.mapper.SourcePlanApiMapperWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.command.create.CreateSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.command.delete.DeleteSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.command.replace.ReplaceSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.query.existence.SourcePlanExistenceQueryWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.query.get.GetSourcePlanServiceWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.query.list.ListSourcePlansServiceWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.application.query.options.adapter.SourcePlanOptionsQueryWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        CreateSourcePlanServiceWiringConfig.class,
        DeleteSourcePlanServiceWiringConfig.class,
        ReplaceSourcePlanServiceWiringConfig.class,
        GetSourcePlanServiceWiringConfig.class,
        ListSourcePlansServiceWiringConfig.class,
        SourcePlanExistenceQueryWiringConfig.class,
        SourcePlanOptionsQueryWiringConfig.class,
        SourcePlanApiMapperWiringConfig.class
})
public class SourcePlanFeatureWiringConfig {
}
