package com.alligator.market.backend.capturer.config.passport.application.sync.startup;

import com.alligator.market.backend.capturer.config.passport.application.sync.service.CapturerPassportStoreSynchronizationServiceWiringConfig;
import com.alligator.market.backend.capturer.passport.application.sync.CapturerPassportStoreSynchronizationService;
import com.alligator.market.backend.capturer.passport.application.sync.runner.CapturerPassportStoreSynchronizationStartupRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        CapturerPassportStoreSynchronizationServiceWiringConfig.class
})
public class CapturerPassportStoreSynchronizationStartupRunnerWiringConfig {
    public static final String BEAN_CAPTURER_PASSPORT_STORE_SYNCHRONIZATION_STARTUP_RUNNER =
            "capturerPassportStoreSynchronizationStartupRunner";

    @Bean(BEAN_CAPTURER_PASSPORT_STORE_SYNCHRONIZATION_STARTUP_RUNNER)
    public ApplicationRunner capturerPassportStoreSynchronizationStartupRunner(
            @Qualifier(CapturerPassportStoreSynchronizationServiceWiringConfig
                    .BEAN_CAPTURER_PASSPORT_STORE_SYNCHRONIZATION_SERVICE)
            CapturerPassportStoreSynchronizationService service
    ) {
        return new CapturerPassportStoreSynchronizationStartupRunner(service);
    }
}
