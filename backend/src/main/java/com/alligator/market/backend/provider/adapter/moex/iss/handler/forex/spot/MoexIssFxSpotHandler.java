package com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssAdapter;
import com.alligator.market.backend.provider.adapter.moex.iss.config.MoexIssAdapterProps;
import com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot.instruments.DomainCodesCatalog;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;
import java.util.Set;

/**
 * <b>Обработчик инструментов FX_SPOT для провайдера MOEX ISS.</b>
 */
public class MoexIssFxSpotHandler extends AbstractInstrumentHandler<MoexIssAdapter, FxSpot> {

    /* Уникальный код обработчика: UPPERCASE, формат [A-Z0-9_]+. */
    private static final String HANDLER_CODE = "MOEX_ISS_FX_SPOT_HANDLER";

    /* Поддерживаемые коды инструментов FX_SPOT. */
    private static final Set<String> SUPPORTED_CODES = DomainCodesCatalog.SUPPORTED_CODES;

    /* Web-клиент. */
    private final WebClient webClient;

    /* Для логирования. */
    private static final Logger log = LoggerFactory.getLogger(MoexIssFxSpotHandler.class);

    //=================================================================================================================
    // КОНСТРУКТОР
    //=================================================================================================================

    /**
     * <b>Конструктор обработчика инструментов FX_SPOT для провайдера MOEX ISS.</b>
     *
     *
     */
    public MoexIssFxSpotHandler(
            MoexIssAdapterProps props,
            WebClient webClient
    ) {
        // Инициализируем базовый класс обработчика инструмента
        super(HANDLER_CODE, FxSpot.class, InstrumentType.FX_SPOT, SUPPORTED_CODES);

        Objects.requireNonNull(props, "props must not be null");
        Objects.requireNonNull(webClient, "webClient must not be null");

        this.webClient = webClient;
    }

    //=================================================================================================================
    // РЕАЛИЗАЦИЯ МЕТОДОВ КОНТРАКТА
    //=================================================================================================================

    /**
     * <b>Чистая логика получения котировки FX_SPOT с MOEX ISS.</b>
     * <p>На первом шаге:</p>
     *  - делаем HTTP-запрос к ISS по secid инструмента;
     *  - логируем сырой JSON-ответ;
     *  - возвращаем пустой Publisher (QuoteTick будет реализован на следующем шаге).
     */
    @Override
    protected Publisher<QuoteTick> doQuote(FxSpot instrument) {
        // Код инструмента (MOEX ISS ожидает формат аналогичный формату доменной модели FxSpot)
        String secid = instrument.instrumentCode();

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/engines/currency/markets/selt/securities/{secid}.json")
                        .queryParam("iss.meta", "off")
                        .queryParam("iss.only", "marketdata")
                        .queryParam("marketdata.columns", "LAST,UPDATETIME")
                        .build(secid)
                )
                .retrieve()
                .bodyToMono(String.class)
                .doOnSubscribe(sub -> log.debug(
                        "Requesting FX_SPOT quote from MOEX ISS: secid={}", secid))
                .doOnNext(body -> log.trace(
                        "Raw MOEX ISS response for {}: {}", secid, body))
                // Пока не мапим JSON в QuoteTick: вернём пустой Publisher
                .flatMap(body -> reactor.core.publisher.Mono.<QuoteTick>empty());
    }
}
