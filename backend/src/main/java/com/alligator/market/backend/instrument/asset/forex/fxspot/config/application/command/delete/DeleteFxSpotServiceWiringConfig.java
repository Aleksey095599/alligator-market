package com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.command.delete;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.delete.DeleteFxSpotService;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.port.FxSpotUsageCheckPort;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.usage.contributor.FxSpotUsageContributorWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.usage.port.FxSpotUsageCheckPortWiringConfig;
import com.alligator.market.backend.instrument.asset.forex.fxspot.config.persistence.repository.adapter.FxSpotRepositoryWiringConfig;
import com.alligator.market.domain.instrument.catalog.forex.fxspot.repository.FxSpotRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Wiring-конфигурация {@link DeleteFxSpotService}.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        FxSpotRepositoryWiringConfig.class,
        FxSpotUsageCheckPortWiringConfig.class,
        FxSpotUsageContributorWiringConfig.class
})
public class DeleteFxSpotServiceWiringConfig {

    public static final String BEAN_DELETE_FX_SPOT_SERVICE = "deleteFxSpotService";

    @Bean(BEAN_DELETE_FX_SPOT_SERVICE)
    public DeleteFxSpotService deleteFxSpotService(
            @Qualifier(FxSpotRepositoryWiringConfig.BEAN_FX_SPOT_REPOSITORY)
            FxSpotRepository fxSpotRepository,
            @Qualifier(FxSpotUsageCheckPortWiringConfig.BEAN_FX_SPOT_USAGE_CHECK_PORT)
            FxSpotUsageCheckPort fxSpotUsageCheckPort
    ) {
        return new DeleteFxSpotService(fxSpotRepository, fxSpotUsageCheckPort);
    }
}
