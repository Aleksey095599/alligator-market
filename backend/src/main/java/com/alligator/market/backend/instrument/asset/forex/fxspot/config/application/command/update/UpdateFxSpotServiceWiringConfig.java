package com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.command.update;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.update.UpdateFxSpotService;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.persistence.repository.adapter.FxSpotRepositoryWiringConfig;
import com.alligator.market.domain.instrument.catalog.forex.fxspot.repository.FxSpotRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link UpdateFxSpotService}.
 */
@Configuration(proxyBeanMethods = false)
@Import(FxSpotRepositoryWiringConfig.class)
public class UpdateFxSpotServiceWiringConfig {

    public static final String BEAN_UPDATE_FX_SPOT_SERVICE = "updateFxSpotService";

    @Bean(BEAN_UPDATE_FX_SPOT_SERVICE)
    public UpdateFxSpotService updateFxSpotService(
            @Qualifier(FxSpotRepositoryWiringConfig.BEAN_FX_SPOT_REPOSITORY)
            FxSpotRepository fxSpotRepository
    ) {
        return new UpdateFxSpotService(fxSpotRepository);
    }
}
