package com.alligator.market.backend.source.config.passport.application.sync.startup;

import com.alligator.market.backend.source.config.passport.application.sync.service.SourcePassportRegistrySynchronizationServiceWiringConfig;
import com.alligator.market.backend.source.passport.application.sync.SourcePassportRegistrySynchronizationService;
import com.alligator.market.backend.source.passport.application.sync.runner.SourcePassportRegistrySynchronizationStartupRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        SourcePassportRegistrySynchronizationServiceWiringConfig.class
})
public class SourcePassportRegistrySynchronizationStartupRunnerWiringConfig {
    public static final String BEAN_SOURCE_PASSPORT_REGISTRY_SYNCHRONIZATION_STARTUP_RUNNER =
            "sourcePassportRegistrySynchronizationStartupRunner";

    @Bean(BEAN_SOURCE_PASSPORT_REGISTRY_SYNCHRONIZATION_STARTUP_RUNNER)
    public ApplicationRunner sourcePassportRegistrySynchronizationStartupRunner(
            @Qualifier(SourcePassportRegistrySynchronizationServiceWiringConfig
                    .BEAN_SOURCE_PASSPORT_REGISTRY_SYNCHRONIZATION_SERVICE)
            SourcePassportRegistrySynchronizationService service
    ) {
        return new SourcePassportRegistrySynchronizationStartupRunner(service);
    }
}
