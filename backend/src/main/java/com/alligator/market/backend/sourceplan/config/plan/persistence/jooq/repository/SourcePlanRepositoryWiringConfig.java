package com.alligator.market.backend.sourceplan.config.plan.persistence.jooq.repository;

import com.alligator.market.backend.sourceplan.plan.persistence.jooq.repository.JooqSourcePlanRepositoryAdapter;
import com.alligator.market.domain.sourceplan.repository.SourcePlanRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring wiring for the {@link SourcePlanRepository} persistence adapter.
 */
@Configuration(proxyBeanMethods = false)
public class SourcePlanRepositoryWiringConfig {

    public static final String BEAN_SOURCE_PLAN_REPOSITORY = "sourcePlanRepository";

    @Bean(BEAN_SOURCE_PLAN_REPOSITORY)
    public SourcePlanRepository sourcePlanRepository(DSLContext dsl) {
        return new JooqSourcePlanRepositoryAdapter(dsl);
    }
}
