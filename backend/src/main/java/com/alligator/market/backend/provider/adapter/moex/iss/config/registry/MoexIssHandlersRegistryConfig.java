package com.alligator.market.backend.provider.adapter.moex.iss.config.registry;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssProvider;
import com.alligator.market.backend.provider.adapter.moex.iss.instrument.type.forex.spot.handler.MoexIssFxSpotHandler;
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
     * Единый реестр всех обработчиков MOEX ISS.
     */
    @Bean("moexIssHandlers")
    public Set<AbstractInstrumentHandler<MoexIssProvider, ? extends Instrument>> moexIssHandlers(
            MoexIssFxSpotHandler fxSpotHandler
    ) {
        return Set.of(fxSpotHandler);
    }
}
