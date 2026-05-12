package com.alligator.market.backend.source.config.passport.application.projection.startup;

import com.alligator.market.backend.source.config.passport.application.projection.service.SourcePassportProjectionServiceWiringConfig;
import com.alligator.market.backend.source.passport.application.projection.SourcePassportProjectionService;
import com.alligator.market.backend.source.passport.application.projection.runner.SourcePassportProjectionStartupRunner;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        SourcePassportProjectionServiceWiringConfig.class
})
public class SourcePassportProjectionStartupRunnerWiringConfig {
    public static final String BEAN_SOURCE_PASSPORT_PROJECTION_STARTUP_RUNNER =
            "sourcePassportProjectionStartupRunner";

    @Bean(BEAN_SOURCE_PASSPORT_PROJECTION_STARTUP_RUNNER)
    public ApplicationRunner sourcePassportProjectionStartupRunner(
            SourcePassportProjectionService service
    ) {
        return new SourcePassportProjectionStartupRunner(service);
    }
}
