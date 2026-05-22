package com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.command.create;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.create.CreateFxSpotService;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.persistence.repository.adapter.FxSpotRepositoryWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.persistence.repository.adapter.CurrencyRepositoryWiringConfig;
import com.alligator.market.backend.instrument.config.registry.sync.RuntimeInstrumentRegistryUpdaterWiringConfig;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.registry.sync.RuntimeInstrumentRegistryUpdater;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        FxSpotRepositoryWiringConfig.class,
        CurrencyRepositoryWiringConfig.class,
        RuntimeInstrumentRegistryUpdaterWiringConfig.class
})
public class CreateFxSpotServiceWiringConfig {
    public static final String BEAN_CREATE_FX_SPOT_SERVICE = "createFxSpotService";

    @Bean(BEAN_CREATE_FX_SPOT_SERVICE)
    public CreateFxSpotService createFxSpotService(
            @Qualifier(FxSpotRepositoryWiringConfig.BEAN_FX_SPOT_REPOSITORY)
            FxSpotRepository fxSpotRepository,
            @Qualifier(CurrencyRepositoryWiringConfig.BEAN_CURRENCY_REPOSITORY)
            CurrencyRepository currencyRepository,
            @Qualifier(RuntimeInstrumentRegistryUpdaterWiringConfig
                    .BEAN_RUNTIME_INSTRUMENT_REGISTRY_UPDATER)
            RuntimeInstrumentRegistryUpdater runtimeInstrumentRegistryUpdater
    ) {
        return new CreateFxSpotService(
                fxSpotRepository,
                currencyRepository,
                runtimeInstrumentRegistryUpdater
        );
    }
}
