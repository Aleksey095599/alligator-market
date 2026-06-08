package com.alligator.market.backend.source.config.passport.application.sync.startup;

import com.alligator.market.backend.source.config.passport.application.sync.service.SourcePassportStoreSynchronizationServiceWiringConfig;
import com.alligator.market.backend.source.passport.application.sync.SourcePassportStoreSynchronizationService;
import com.alligator.market.backend.source.passport.application.sync.runner.SourcePassportStoreSynchronizationStartupRunner;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        SourcePassportStoreSynchronizationServiceWiringConfig.class
})
public class SourcePassportStoreSynchronizationStartupRunnerWiringConfig {
    public static final String BEAN_SOURCE_PASSPORT_STORE_SYNCHRONIZATION_STARTUP_RUNNER =
            "sourcePassportStoreSynchronizationStartupRunner";

    @Bean(BEAN_SOURCE_PASSPORT_STORE_SYNCHRONIZATION_STARTUP_RUNNER)
    public ApplicationRunner sourcePassportStoreSynchronizationStartupRunner(
            @Qualifier(SourcePassportStoreSynchronizationServiceWiringConfig
                    .BEAN_SOURCE_PASSPORT_STORE_SYNCHRONIZATION_SERVICE)
            SourcePassportStoreSynchronizationService service
    ) {
        return new SourcePassportStoreSynchronizationStartupRunner(service);
    }
}
