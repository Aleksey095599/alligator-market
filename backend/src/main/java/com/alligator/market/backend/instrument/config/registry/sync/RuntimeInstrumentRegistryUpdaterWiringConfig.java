package com.alligator.market.backend.instrument.config.registry.sync;

import com.alligator.market.backend.instrument.asset.forex.fxspot.config.persistence.repository.adapter.FxSpotRepositoryWiringConfig;
import com.alligator.market.backend.instrument.config.registry.runtime.RuntimeInstrumentRegistryWiringConfig;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.registry.sync.RuntimeInstrumentRegistryPublisher;
import com.alligator.market.domain.instrument.registry.sync.RuntimeInstrumentRegistryUpdater;
import com.alligator.market.domain.instrument.registry.sync.SnapshotRuntimeInstrumentRegistryUpdater;
import com.alligator.market.domain.instrument.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@Import({
        FxSpotRepositoryWiringConfig.class,
        RuntimeInstrumentRegistryWiringConfig.class
})
public class RuntimeInstrumentRegistryUpdaterWiringConfig {
    public static final String BEAN_RUNTIME_INSTRUMENT_REGISTRY_UPDATER =
            "runtimeInstrumentRegistryUpdater";

    @Bean(BEAN_RUNTIME_INSTRUMENT_REGISTRY_UPDATER)
    public RuntimeInstrumentRegistryUpdater runtimeInstrumentRegistryUpdater(
            List<InstrumentRepository<? extends Instrument>> repositories,
            @Qualifier(RuntimeInstrumentRegistryWiringConfig.BEAN_RUNTIME_INSTRUMENT_REGISTRY_PUBLISHER)
            RuntimeInstrumentRegistryPublisher runtimeRegistryPublisher
    ) {
        return new SnapshotRuntimeInstrumentRegistryUpdater(
                repositories,
                runtimeRegistryPublisher
        );
    }
}
