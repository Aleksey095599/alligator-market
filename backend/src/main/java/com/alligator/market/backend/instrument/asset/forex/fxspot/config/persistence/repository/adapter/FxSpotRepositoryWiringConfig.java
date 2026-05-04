package com.alligator.market.backend.instrument.asset.forex.fxspot.config.persistence.repository.adapter;

import com.alligator.market.backend.instrument.asset.forex.fxspot.persistence.jooq.repository.JooqFxSpotRepositoryAdapter;
import com.alligator.market.domain.instrument.catalog.forex.fxspot.repository.FxSpotRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link FxSpotRepository}.
 */
@Configuration(proxyBeanMethods = false)
public class FxSpotRepositoryWiringConfig {

    public static final String BEAN_FX_SPOT_REPOSITORY = "fxSpotRepository";

    @Bean(BEAN_FX_SPOT_REPOSITORY)
    public FxSpotRepository fxSpotRepository(DSLContext dsl) {
        return new JooqFxSpotRepositoryAdapter(dsl);
    }
}
