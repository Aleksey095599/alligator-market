package com.alligator.market.backend.source.config.passport.application.query.list;

import com.alligator.market.backend.source.passport.application.query.list.PassportListService;
import com.alligator.market.backend.source.passport.application.query.list.port.MarketDataSourcePassportListQueryPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring configuration for {@link PassportListService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataSourcePassportListQueryPortWiringConfig.class
})
public class PassportListServiceWiringConfig {

    public static final String BEAN_PASSPORT_LIST_SERVICE = "passportListService";

    @Bean(BEAN_PASSPORT_LIST_SERVICE)
    public PassportListService passportListService(
            @Qualifier(MarketDataSourcePassportListQueryPortWiringConfig
                    .BEAN_MARKET_DATA_SOURCE_PASSPORT_LIST_QUERY_PORT)
            MarketDataSourcePassportListQueryPort queryPort
    ) {
        return new PassportListService(queryPort);
    }
}
