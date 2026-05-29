package com.alligator.market.backend.capturer.config.passport.application.query.list;

import com.alligator.market.backend.capturer.passport.application.query.list.CapturerPassportListService;
import com.alligator.market.backend.capturer.passport.application.query.list.port.CapturerPassportListQueryPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        CapturerPassportListQueryPortWiringConfig.class
})
public class CapturerPassportListServiceWiringConfig {
    public static final String BEAN_CAPTURER_PASSPORT_LIST_SERVICE =
            "capturerPassportListService";

    @Bean(BEAN_CAPTURER_PASSPORT_LIST_SERVICE)
    public CapturerPassportListService capturerPassportListService(
            @Qualifier(CapturerPassportListQueryPortWiringConfig
                    .BEAN_CAPTURER_PASSPORT_LIST_QUERY_PORT)
            CapturerPassportListQueryPort queryPort
    ) {
        return new CapturerPassportListService(queryPort);
    }
}
