package com.alligator.market.backend;

import com.alligator.market.backend.instrument.asset.forex.fxspot.config.FxSpotFeatureWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.config.CurrencyFeatureWiringConfig;
import com.alligator.market.backend.marketdata.config.capture.process.MarketDataCaptureProcessFeatureWiringConfig;
import com.alligator.market.backend.source.config.MarketDataSourceFeatureWiringConfig;
import com.alligator.market.backend.sourceplan.config.plan.SourcingPlanFeatureWiringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * Точка входа приложения.
 */
@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan("com.alligator.market.backend")
@Import({
        MarketDataSourceFeatureWiringConfig.class,
        MarketDataCaptureProcessFeatureWiringConfig.class,
        SourcingPlanFeatureWiringConfig.class,
        FxSpotFeatureWiringConfig.class,
        CurrencyFeatureWiringConfig.class
})
public class BackendApplication {

    private static final TimeZone TECHNICAL_TIME_ZONE = TimeZone.getTimeZone(ZoneOffset.UTC);

    public static void main(String[] args) {
        // Глобальная техническая зона JVM для всего приложения.
        TimeZone.setDefault(TECHNICAL_TIME_ZONE);

        SpringApplication.run(BackendApplication.class, args);
    }
}
