package com.alligator.market.backend.provider.adapter.moex.iss.handler.registry;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssAdapter;
import com.alligator.market.backend.provider.adapter.moex.iss.handler.instrument.forex.spot.MoexIssFxSpotHandler;
import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.provider.model.handler.AbstractInstrumentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

/**
 * Конфигурация бина реестра обработчиков провайдера MOEX ISS.
 */
@Configuration(proxyBeanMethods = false)
public class MoexIssHandlersRegistryConfig {

    /**
     * Единый реестр обработчиков MOEX ISS.
     */
    @Bean("moexIssHandlers")
    public Set<AbstractInstrumentHandler<MoexIssAdapter, ? extends Instrument>> moexIssHandlers(
            MoexIssFxSpotHandler fxSpotHandler
    ) {
        return Set.of(fxSpotHandler);
    }
}
