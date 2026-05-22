package com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.command.update;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.update.UpdateFxSpotService;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.persistence.repository.adapter.FxSpotRepositoryWiringConfig;
import com.alligator.market.backend.instrument.config.registry.sync.RuntimeInstrumentRegistryUpdaterWiringConfig;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.registry.sync.RuntimeInstrumentRegistryUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        FxSpotRepositoryWiringConfig.class,
        RuntimeInstrumentRegistryUpdaterWiringConfig.class
})
public class UpdateFxSpotServiceWiringConfig {
    public static final String BEAN_UPDATE_FX_SPOT_SERVICE = "updateFxSpotService";

    @Bean(BEAN_UPDATE_FX_SPOT_SERVICE)
    public UpdateFxSpotService updateFxSpotService(
            @Qualifier(FxSpotRepositoryWiringConfig.BEAN_FX_SPOT_REPOSITORY)
            FxSpotRepository fxSpotRepository,
            @Qualifier(RuntimeInstrumentRegistryUpdaterWiringConfig
                    .BEAN_RUNTIME_INSTRUMENT_REGISTRY_UPDATER)
            RuntimeInstrumentRegistryUpdater runtimeInstrumentRegistryUpdater
    ) {
        return new UpdateFxSpotService(fxSpotRepository, runtimeInstrumentRegistryUpdater);
    }
}
