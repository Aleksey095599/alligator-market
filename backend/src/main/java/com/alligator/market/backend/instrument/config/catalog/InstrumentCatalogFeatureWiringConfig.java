package com.alligator.market.backend.instrument.config.catalog;

import com.alligator.market.backend.instrument.config.catalog.application.query.InstrumentCatalogServiceWiringConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import(InstrumentCatalogServiceWiringConfig.class)
public class InstrumentCatalogFeatureWiringConfig {
}
