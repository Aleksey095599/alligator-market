package com.alligator.market.backend;

import com.alligator.market.backend.capturer.config.passport.application.sync.startup.CapturerPassportRegistrySynchronizationStartupRunnerWiringConfig;
import com.alligator.market.backend.capturer.config.passport.application.query.list.MarketDataCapturerPassportListServiceWiringConfig;
import com.alligator.market.backend.capturer.config.registry.RuntimeCapturerRegistryWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.FxSpotFeatureWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.CurrencyFeatureWiringConfig;
import com.alligator.market.backend.instrument.config.catalog.InstrumentCatalogFeatureWiringConfig;
import com.alligator.market.backend.instrument.config.registry.InstrumentRegistryFeatureWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.QuoteMonitorFeatureWiringConfig;
import com.alligator.market.backend.source.config.SourceFeatureWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.SourcePlanFeatureWiringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.ZoneOffset;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan("com.alligator.market.backend")
@Import({
        SourceFeatureWiringConfig.class,
        InstrumentRegistryFeatureWiringConfig.class,
        InstrumentCatalogFeatureWiringConfig.class,
        RuntimeCapturerRegistryWiringConfig.class,
        MarketDataCapturerPassportListServiceWiringConfig.class,
        CapturerPassportRegistrySynchronizationStartupRunnerWiringConfig.class,
        QuoteMonitorFeatureWiringConfig.class,
        SourcePlanFeatureWiringConfig.class,
        FxSpotFeatureWiringConfig.class,
        CurrencyFeatureWiringConfig.class
})
public class BackendApplication {
    private static final TimeZone TECHNICAL_TIME_ZONE = TimeZone.getTimeZone(ZoneOffset.UTC);

    public static void main(String[] args) {
        // Keep JVM technical time in UTC; business time zones are injected explicitly.
        TimeZone.setDefault(TECHNICAL_TIME_ZONE);

        SpringApplication.run(BackendApplication.class, args);
    }
}
