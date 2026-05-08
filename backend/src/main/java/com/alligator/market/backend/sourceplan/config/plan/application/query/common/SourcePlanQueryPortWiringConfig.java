package com.alligator.market.backend.sourceplan.config.plan.application.query.common;

import com.alligator.market.backend.sourceplan.plan.application.query.common.adapter.JooqSourcePlanQueryAdapter;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.SourcePlanQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация read-side запросов source plan для административного API.
 */
@Configuration(proxyBeanMethods = false)
public class SourcePlanQueryPortWiringConfig {

    public static final String BEAN_SOURCE_PLAN_QUERY_PORT =
            "sourcePlanQueryPort";

    @Bean(BEAN_SOURCE_PLAN_QUERY_PORT)
    public SourcePlanQueryPort sourcePlanQueryPort(DSLContext dsl) {
        return new JooqSourcePlanQueryAdapter(dsl);
    }
}
