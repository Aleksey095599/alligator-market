package com.alligator.market.backend.source.config.passport.application.query.list;

import com.alligator.market.backend.source.passport.application.query.list.adapter.JooqSourcePassportListQueryAdapter;
import com.alligator.market.backend.source.passport.application.query.list.port.SourcePassportListQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SourcePassportListQueryPortWiringConfig {
    public static final String BEAN_SOURCE_PASSPORT_LIST_QUERY_PORT =
            "sourcePassportListQueryPort";

    @Bean(BEAN_SOURCE_PASSPORT_LIST_QUERY_PORT)
    public SourcePassportListQueryPort sourcePassportListQueryPort(DSLContext dsl) {
        return new JooqSourcePassportListQueryAdapter(dsl);
    }
}
