package com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssAdapter;
import com.alligator.market.backend.provider.adapter.moex.iss.config.MoexIssAdapterProps;
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
    private static final Set<String> SUPPORTED_CODES = MoexIssFxSpotInstruments.SUPPORTED_DOMAIN_CODES;

    /* Web-клиент. */
    private final WebClient webClient;

    /* Для логирования. */
    private static final Logger log = LoggerFactory.getLogger(MoexIssFxSpotHandler.class);

    //=================================================================================================================
    // КОНСТРУКТОР
    //=================================================================================================================

    /**
     * <b>Конструктор обработчика инструментов FX_SPOT для провайдера MOEX ISS.</b>
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
     * <ul>
     *   <li>Получаем SECID MOEX ISS по доменному коду инструмента;</li>
     *   <li>Делаем HTTP-запрос к ISS по этому SECID;</li>
     *   <li>Логируем сырой JSON-ответ;</li>
     *   <li>Возвращаем пустой Publisher (QuoteTick будет реализован на следующем шаге).</li>
     * </ul>
     */
    @Override
    protected Publisher<QuoteTick> doQuote(FxSpot instrument) {
        // Доменный код инструмента уже проверен абстрактным обработчиком
        String domainCode = instrument.instrumentCode();

        // Конвертируем доменный код в SECID MOEX ISS
        String secid = MoexIssFxSpotInstruments.moexSecidOf(domainCode);

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
                .bodyToMono(String.class) // Пока берём сырой JSON как строку
                .doOnSubscribe(sub -> log.debug(
                        "Requesting FX_SPOT quote from MOEX ISS: instrumentCode={}, secid={}",
                        domainCode, secid))
                .doOnNext(body -> log.trace(
                        "Raw MOEX ISS response for instrumentCode={}, secid={}: {}",
                        domainCode, secid, body))
                // Пока не мапим JSON в QuoteTick: вернём пустой Publisher
                .flatMap(body -> reactor.core.publisher.Mono.<QuoteTick>empty());
    }
}
