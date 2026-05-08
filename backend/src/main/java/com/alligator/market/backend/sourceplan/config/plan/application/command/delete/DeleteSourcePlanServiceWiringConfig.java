package com.alligator.market.backend.sourceplan.config.plan.application.command.delete;

import com.alligator.market.backend.sourceplan.config.plan.persistence.jooq.repository.SourcePlanRepositoryWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.command.delete.DeleteSourcePlanService;
import com.alligator.market.domain.sourceplan.repository.SourcePlanRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        SourcePlanRepositoryWiringConfig.class
})
public class DeleteSourcePlanServiceWiringConfig {
    public static final String BEAN_DELETE_SOURCE_PLAN_SERVICE = "deleteSourcePlanService";

    @Bean(BEAN_DELETE_SOURCE_PLAN_SERVICE)
    public DeleteSourcePlanService deleteSourcePlanService(
            @Qualifier(SourcePlanRepositoryWiringConfig.BEAN_SOURCE_PLAN_REPOSITORY)
            SourcePlanRepository sourcePlanRepository
    ) {
        return new DeleteSourcePlanService(sourcePlanRepository);
    }
}
