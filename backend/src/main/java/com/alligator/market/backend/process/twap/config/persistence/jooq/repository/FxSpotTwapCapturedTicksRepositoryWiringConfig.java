package com.alligator.market.backend.process.twap.config.persistence.jooq.repository;

import com.alligator.market.backend.process.twap.infra.persistence.jooq.repository.JooqFxSpotTwapCapturedTicksRepositoryAdapter;
import com.alligator.market.domain.process.twap.repository.FxSpotTwapCapturedTicksRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring wiring for the FX Spot TWAP captured ticks repository port.
 */
@Configuration(proxyBeanMethods = false)
public class FxSpotTwapCapturedTicksRepositoryWiringConfig {

    public static final String BEAN_FX_SPOT_TWAP_CAPTURED_TICKS_REPOSITORY =
            "fxSpotTwapCapturedTicksRepository";

    @Bean(BEAN_FX_SPOT_TWAP_CAPTURED_TICKS_REPOSITORY)
    public FxSpotTwapCapturedTicksRepository fxSpotTwapCapturedTicksRepository(DSLContext dsl) {
        return new JooqFxSpotTwapCapturedTicksRepositoryAdapter(dsl);
    }
}
