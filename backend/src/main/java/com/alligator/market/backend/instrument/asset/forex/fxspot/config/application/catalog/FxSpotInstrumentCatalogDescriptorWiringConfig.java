package com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.catalog;

import com.alligator.market.backend.instrument.application.catalog.descriptor.InstrumentCatalogDescriptor;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.catalog.FxSpotInstrumentCatalogDescriptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class FxSpotInstrumentCatalogDescriptorWiringConfig {
    public static final String BEAN_FX_SPOT_INSTRUMENT_CATALOG_DESCRIPTOR =
            "fxSpotInstrumentCatalogDescriptor";

    @Bean(BEAN_FX_SPOT_INSTRUMENT_CATALOG_DESCRIPTOR)
    public InstrumentCatalogDescriptor fxSpotInstrumentCatalogDescriptor() {
        return new FxSpotInstrumentCatalogDescriptor();
    }
}
