package com.alligator.market.backend.instrument.asset.forex.fxspot.config.persistence.repository.adapter;

import com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.persistence.jpa.FxSpotJpaRepository;
import com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.persistence.repository.FxSpotRepositoryAdapter;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wiring-конфигурация {@link FxSpotRepository}.
 */
@Configuration(proxyBeanMethods = false)
public class FxSpotRepositoryWiringConfig {

    public static final String BEAN_FX_SPOT_REPOSITORY = "fxSpotRepository";

    @Bean(BEAN_FX_SPOT_REPOSITORY)
    public FxSpotRepository fxSpotRepository(
            FxSpotJpaRepository fxSpotJpaRepository,
            CurrencyRepository currencyRepository
    ) {
        return new FxSpotRepositoryAdapter(fxSpotJpaRepository, currencyRepository);
    }
}
