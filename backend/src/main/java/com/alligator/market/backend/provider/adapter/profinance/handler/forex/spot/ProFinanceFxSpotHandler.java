package com.alligator.market.backend.provider.adapter.profinance.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.profinance.ProFinanceAdapter;
import com.alligator.market.backend.provider.adapter.profinance.config.ProFinanceAdapterProps;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public class ProFinanceFxSpotHandler extends AbstractInstrumentHandler<ProFinanceAdapter, FxSpot> {

    /* Уникальный код обработчика: UPPERCASE, формат [A-Z0-9_]+. */
    private static final String HANDLER_CODE = "PROFINANCE_FX_SPOT_HANDLER";

    /* Поддерживаемые коды инструментов FX_SPOT. */
    private static final Set<String> SUPPORTED_CODES = ProFinanceFxSpotCatalog.SUPPORTED_CODES;

    /* Web-клиент. */
    private final WebClient webClient;

    /* Параметры подключения к провайдеру. */
    private final ProFinanceAdapterProps props;

    /**
     * Конструктор.
     */
    public ProFinanceFxSpotHandler(
            WebClient webClient,
            ProFinanceAdapterProps props
    ) {
        // Конструктор материнского класса обработчика
        super(HANDLER_CODE, FxSpot.class, InstrumentType.FX_SPOT, SUPPORTED_CODES);

        Objects.requireNonNull(webClient, "webClient must not be null");
        Objects.requireNonNull(props, "props must not be null");

        this.webClient = webClient;
        this.props = props;
    }

    /**
     * Реализация получения котировки.
     */
    @Override
    protected Publisher<QuoteTick> doQuote(FxSpot instrument) {
        // Временно возвращаем синтетические тики раз в minUpdateInterval — для проверки
        Duration period = provider().policy().minUpdateInterval();
        return Flux.interval(Duration.ZERO, period)
                .map(t -> new QuoteTick(
                        instrument.instrumentCode(),
                        new BigDecimal("1.0000"), // TODO: bid из HTML
                        new BigDecimal("1.0005"), // TODO: ask из HTML
                        Instant.now(),
                        provider().providerCode()
                ));
    }
}
