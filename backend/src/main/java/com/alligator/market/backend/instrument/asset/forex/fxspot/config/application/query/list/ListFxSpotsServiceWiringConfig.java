package com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.query.list;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.list.ListFxSpotsService;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.persistence.repository.adapter.FxSpotRepositoryWiringConfig;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link ListFxSpotsService}.
 */
@Configuration(proxyBeanMethods = false)
@Import(FxSpotRepositoryWiringConfig.class)
public class ListFxSpotsServiceWiringConfig {

    public static final String BEAN_LIST_FX_SPOTS_SERVICE = "listFxSpotsService";

    @Bean(BEAN_LIST_FX_SPOTS_SERVICE)
    public ListFxSpotsService listFxSpotsService(
            @Qualifier(FxSpotRepositoryWiringConfig.BEAN_FX_SPOT_REPOSITORY)
            FxSpotRepository fxSpotRepository
    ) {
        return new ListFxSpotsService(fxSpotRepository);
    }
}
