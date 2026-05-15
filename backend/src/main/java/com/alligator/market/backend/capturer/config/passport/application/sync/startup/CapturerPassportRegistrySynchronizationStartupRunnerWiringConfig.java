package com.alligator.market.backend.capturer.config.passport.application.sync.startup;

import com.alligator.market.backend.capturer.config.passport.application.sync.service.CapturerPassportRegistrySynchronizationServiceWiringConfig;
import com.alligator.market.backend.capturer.passport.application.sync.CapturerPassportRegistrySynchronizationService;
import com.alligator.market.backend.capturer.passport.application.sync.runner.CapturerPassportRegistrySynchronizationStartupRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        CapturerPassportRegistrySynchronizationServiceWiringConfig.class
})
public class CapturerPassportRegistrySynchronizationStartupRunnerWiringConfig {
    public static final String BEAN_CAPTURER_PASSPORT_REGISTRY_SYNCHRONIZATION_STARTUP_RUNNER =
            "capturerPassportRegistrySynchronizationStartupRunner";

    @Bean(BEAN_CAPTURER_PASSPORT_REGISTRY_SYNCHRONIZATION_STARTUP_RUNNER)
    public ApplicationRunner capturerPassportRegistrySynchronizationStartupRunner(
            @Qualifier(CapturerPassportRegistrySynchronizationServiceWiringConfig
                    .BEAN_CAPTURER_PASSPORT_REGISTRY_SYNCHRONIZATION_SERVICE)
            CapturerPassportRegistrySynchronizationService service
    ) {
        return new CapturerPassportRegistrySynchronizationStartupRunner(service);
    }
}
