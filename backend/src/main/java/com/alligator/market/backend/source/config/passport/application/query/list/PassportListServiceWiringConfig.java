package com.alligator.market.backend.source.config.passport.application.query.list;

import com.alligator.market.backend.source.passport.application.query.list.PassportListService;
import com.alligator.market.backend.source.passport.application.query.list.port.ProviderPassportListQueryPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring configuration for {@link PassportListService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        ProviderPassportListQueryPortWiringConfig.class
})
public class PassportListServiceWiringConfig {

    public static final String BEAN_PASSPORT_LIST_SERVICE = "passportListService";

    @Bean(BEAN_PASSPORT_LIST_SERVICE)
    public PassportListService passportListService(
            @Qualifier(ProviderPassportListQueryPortWiringConfig.BEAN_PROVIDER_PASSPORT_LIST_QUERY_PORT)
            ProviderPassportListQueryPort queryPort
    ) {
        return new PassportListService(queryPort);
    }
}
