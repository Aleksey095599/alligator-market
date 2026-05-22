package com.alligator.market.backend.instrument.config.catalog.application.query;

import com.alligator.market.backend.instrument.application.catalog.InstrumentCatalogService;
import com.alligator.market.backend.instrument.application.catalog.descriptor.InstrumentCatalogDescriptor;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.catalog.FxSpotInstrumentCatalogDescriptorWiringConfig;
import com.alligator.market.backend.instrument.config.registry.runtime.RuntimeInstrumentRegistryWiringConfig;
import com.alligator.market.domain.instrument.registry.runtime.RuntimeInstrumentRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

@Configuration(proxyBeanMethods = false)
@Import({
        RuntimeInstrumentRegistryWiringConfig.class,
        FxSpotInstrumentCatalogDescriptorWiringConfig.class
})
public class InstrumentCatalogServiceWiringConfig {
    public static final String BEAN_INSTRUMENT_CATALOG_SERVICE = "instrumentCatalogService";

    @Bean(BEAN_INSTRUMENT_CATALOG_SERVICE)
    public InstrumentCatalogService instrumentCatalogService(
            @Qualifier(RuntimeInstrumentRegistryWiringConfig.BEAN_RUNTIME_INSTRUMENT_REGISTRY)
            RuntimeInstrumentRegistry runtimeInstrumentRegistry,
            List<InstrumentCatalogDescriptor> descriptors
    ) {
        return new InstrumentCatalogService(runtimeInstrumentRegistry, descriptors);
    }
}
