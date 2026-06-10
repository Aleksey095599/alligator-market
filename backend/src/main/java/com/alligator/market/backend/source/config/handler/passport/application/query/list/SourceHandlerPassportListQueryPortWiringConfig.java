package com.alligator.market.backend.source.config.handler.passport.application.query.list;

import com.alligator.market.backend.source.handler.passport.application.query.list.adapter.JooqSourceHandlerPassportListQueryAdapter;
import com.alligator.market.backend.source.handler.passport.application.query.list.port.SourceHandlerPassportListQueryPort;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SourceHandlerPassportListQueryPortWiringConfig {
    public static final String BEAN_SOURCE_HANDLER_PASSPORT_LIST_QUERY_PORT =
            "sourceHandlerPassportListQueryPort";

    @Bean(BEAN_SOURCE_HANDLER_PASSPORT_LIST_QUERY_PORT)
    public SourceHandlerPassportListQueryPort sourceHandlerPassportListQueryPort(DSLContext dsl) {
        return new JooqSourceHandlerPassportListQueryAdapter(dsl);
    }
}
