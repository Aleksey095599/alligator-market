package com.alligator.market.backend.source.config.handler.passport.application.query.list;

import com.alligator.market.backend.source.handler.passport.application.query.list.SourceHandlerPassportListService;
import com.alligator.market.backend.source.handler.passport.application.query.list.port.SourceHandlerPassportListQueryPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        SourceHandlerPassportListQueryPortWiringConfig.class
})
public class SourceHandlerPassportListServiceWiringConfig {
    public static final String BEAN_SOURCE_HANDLER_PASSPORT_LIST_SERVICE =
            "sourceHandlerPassportListService";

    @Bean(BEAN_SOURCE_HANDLER_PASSPORT_LIST_SERVICE)
    public SourceHandlerPassportListService sourceHandlerPassportListService(
            @Qualifier(SourceHandlerPassportListQueryPortWiringConfig
                    .BEAN_SOURCE_HANDLER_PASSPORT_LIST_QUERY_PORT)
            SourceHandlerPassportListQueryPort queryPort
    ) {
        return new SourceHandlerPassportListService(queryPort);
    }
}
