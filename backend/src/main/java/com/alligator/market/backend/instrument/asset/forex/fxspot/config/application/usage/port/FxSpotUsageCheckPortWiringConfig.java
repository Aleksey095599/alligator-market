package com.alligator.market.backend.instrument.asset.forex.fxspot.config.application.usage.port;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.contributor.FxSpotUsageContributor;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.port.FxSpotUsageCheckPort;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.usage.port.adapter.FxSpotUsageCheckAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Wiring-конфигурация {@link FxSpotUsageCheckPort}.
 */
@Configuration(proxyBeanMethods = false)
public class FxSpotUsageCheckPortWiringConfig {

    public static final String BEAN_FX_SPOT_USAGE_CHECK_PORT = "fxSpotUsageCheckPort";

    @Bean(BEAN_FX_SPOT_USAGE_CHECK_PORT)
    public FxSpotUsageCheckPort fxSpotUsageCheckPort(List<FxSpotUsageContributor> contributors) {
        return new FxSpotUsageCheckAdapter(contributors);
    }
}
