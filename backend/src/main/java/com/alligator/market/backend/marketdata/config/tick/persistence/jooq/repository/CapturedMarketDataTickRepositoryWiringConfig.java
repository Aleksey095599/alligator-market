package com.alligator.market.backend.marketdata.config.tick.persistence.jooq.repository;

import com.alligator.market.backend.marketdata.tick.persistence.jooq.repository.JooqCapturedMarketDataTickRepositoryAdapter;
import com.alligator.market.domain.marketdata.tick.level.capture.repository.CapturedMarketDataTickRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link CapturedMarketDataTickRepository}.
 */
@Configuration(proxyBeanMethods = false)
public class CapturedMarketDataTickRepositoryWiringConfig {

    public static final String BEAN_CAPTURED_MARKET_DATA_TICK_REPOSITORY =
            "capturedMarketDataTickRepository";

    @Bean(BEAN_CAPTURED_MARKET_DATA_TICK_REPOSITORY)
    public CapturedMarketDataTickRepository capturedMarketDataTickRepository(DSLContext dsl) {
        return new JooqCapturedMarketDataTickRepositoryAdapter(dsl);
    }
}
