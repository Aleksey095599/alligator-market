package com.alligator.market.backend.capturer.config.passport.application.query.list;

import com.alligator.market.backend.capturer.passport.application.query.list.MarketDataCapturerPassportListService;
import com.alligator.market.backend.capturer.passport.application.query.list.port.MarketDataCapturerPassportListQueryPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring configuration for {@link MarketDataCapturerPassportListService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MarketDataCapturerPassportListQueryPortWiringConfig.class
})
public class MarketDataCapturerPassportListServiceWiringConfig {

    public static final String BEAN_CAPTURER_PASSPORT_LIST_SERVICE =
            "capturerPassportListService";

    @Bean(BEAN_CAPTURER_PASSPORT_LIST_SERVICE)
    public MarketDataCapturerPassportListService capturerPassportListService(
            @Qualifier(MarketDataCapturerPassportListQueryPortWiringConfig
                    .BEAN_CAPTURER_PASSPORT_LIST_QUERY_PORT)
            MarketDataCapturerPassportListQueryPort queryPort
    ) {
        return new MarketDataCapturerPassportListService(queryPort);
    }
}
