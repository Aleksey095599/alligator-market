package com.alligator.market.backend.capturer.config.passport.application.query.list;

import com.alligator.market.backend.capturer.passport.application.query.list.adapter.JooqCapturerPassportListQueryAdapter;
import com.alligator.market.backend.capturer.passport.application.query.list.port.CapturerPassportListQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class CapturerPassportListQueryPortWiringConfig {
    public static final String BEAN_CAPTURER_PASSPORT_LIST_QUERY_PORT =
            "capturerPassportListQueryPort";

    @Bean(BEAN_CAPTURER_PASSPORT_LIST_QUERY_PORT)
    public CapturerPassportListQueryPort capturerPassportListQueryPort(DSLContext dsl) {
        return new JooqCapturerPassportListQueryAdapter(dsl);
    }
}
