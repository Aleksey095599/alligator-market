package com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.usage.contributor.sourcing;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor.sourcing.adapter.JooqSourcePlanExistenceQueryAdapter;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor.sourcing.port.SourcePlanExistenceQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link SourcePlanExistenceQueryPort}.
 */
@Configuration(proxyBeanMethods = false)
public class SourcePlanExistenceQueryWiringConfig {

    public static final String BEAN_SOURCE_PLAN_EXISTENCE_QUERY_PORT = "sourcePlanExistenceQueryPort";

    @Bean(BEAN_SOURCE_PLAN_EXISTENCE_QUERY_PORT)
    public SourcePlanExistenceQueryPort sourcePlanExistenceQueryPort(DSLContext dsl) {
        return new JooqSourcePlanExistenceQueryAdapter(dsl);
    }
}
