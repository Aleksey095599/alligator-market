package com.alligator.market.backend.provider.adapter.moex.iss.config.handlers;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssProvider;
import com.alligator.market.backend.provider.adapter.moex.iss.config.instrument.forex.spot.handler.MoexIssFxSpotHandlerConfig;
import com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.handler.MoexIssFxSpotHandler;
import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.provider.model.handler.AbstractInstrumentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Set;

/**
 * Конфигурация бина набора ВСЕХ обработчиков провайдера MOEX ISS.
 */
@Configuration(proxyBeanMethods = false)
@Import({
        MoexIssFxSpotHandlerConfig.class
        // + другие *HandlerConfig по мере добавления
})
public class MoexIssHandlersConfig {

    public static final String BEAN_HANDLERS = "moexIssHandlers";

    /**
     * Единый набор ВСЕХ обработчиков MOEX ISS.
     */
    @Bean(BEAN_HANDLERS)
    public Set<AbstractInstrumentHandler<MoexIssProvider, ? extends Instrument>> moexIssHandlers(
            MoexIssFxSpotHandler fxSpotHandler
    ) {
        return Set.of(fxSpotHandler);
    }
}
