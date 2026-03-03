package com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.handler;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssProvider;
import com.alligator.market.backend.provider.adapter.moex.iss.instrument.forex.spot.support.MoexIssFxSpotSupportCatalog;
import com.alligator.market.domain.instrument.asset.InstrumentType;
import com.alligator.market.domain.instrument.asset.forex.type.spot.model.FxSpot;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.provider.model.passport.AccessMethod;
import com.alligator.market.domain.provider.model.vo.HandlerCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Set;

/**
 * Обработчик инструментов FX_SPOT провайдера MOEX ISS {@link MoexIssProvider}.
 */
@Slf4j
public class MoexIssFxSpotHandler extends AbstractInstrumentHandler<MoexIssProvider, FxSpot> {

    //region FIELDS

    /* Код обработчика. */
    private static final HandlerCode HANDLER_CODE = HandlerCode.of("MOEX_ISS_FX_SPOT_HANDLER");

    /* Поддерживаемые коды инструментов FX_SPOT. */
    private static final Set<InstrumentCode> SUPPORTED_CODES = MoexIssFxSpotSupportCatalog.SUPPORTED_DOMAIN_CODES;

    /* Web-клиент. */
    private final WebClient webClient;

    /* Формат даты/времени поля SYSTIME в ответе MOEX ISS. */
    private static final DateTimeFormatter MOEX_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /* Временная зона MOEX (используется для конвертации SYSTIME в Instant). */
    private static final ZoneId MOEX_ZONE = ZoneId.of("Europe/Moscow");

    //endregion

    //region CONSTRUCTION

    /**
     * Конструктор обработчика.
     *
     * @param webClient web-клиент настроенный для запросов провайдеру MOEX ISS по инструментам типа FX_SPOT
     */
    public MoexIssFxSpotHandler(WebClient webClient) {
        super(HANDLER_CODE, FxSpot.class, InstrumentType.FX_SPOT, SUPPORTED_CODES);

        Objects.requireNonNull(webClient, "webClient must not be null");
        this.webClient = webClient;
    }

    //endregion

    //region TEMPLATE METHOD

    /**
     * Чистая логика получения потока котировок для переданного инструмента FX_SPOT.
     *
     * <p>Реализован метод доступа API_POLL {@link AccessMethod#API_POLL}:
     * один запрос --> один тик --> пауза согласно "политике" провайдера --> повтор.</p>
     */
    @Override
    protected Publisher<QuoteTick> doQuote(FxSpot instrument) {
        // 1) Получаем минимальный интервал обновления из "политики" провайдера
        Duration pollInterval = provider().policy().minUpdateInterval();

        // Далее в виде цепочки:
        // 2) Запрашиваем котировку (один раз)
        // 3) Ошибки не “убивают” поток: логируем и пропускаем тик
        // 4) Повторяем с задержкой согласно "политике" провайдера
        return fetchQuoteOnce(instrument)
                .onErrorResume(ex -> {
                    log.warn(
                            "Failed to fetch FX_SPOT quote from MOEX ISS: instrumentCode={}, reason={}",
                            instrument.instrumentCode().value(),
                            ex.getMessage(),
                            ex
                    );
                    return Mono.empty();
                })
                .repeatWhen(completed -> completed.delayElements(pollInterval));
    }

    //endregion

    //region INTERNALS

    /**
     * Один запрос к MOEX ISS --> одна котировка инструмента FX_SPOT.
     *
     * <p>Алгоритм:
     * <ul>
     *   <li>Код инструмента конвертируем в SECID, который ожидает MOEX ISS;</li>
     *   <li>Запрашиваем у MOEX ISS таблицу {@code marketdata} для кода инструмента SECID (примечание: запрос на board CETS);</li>
     *   <li>Строго проверяем структуру JSON и извлекаем {@code SYSTIME} и {@code LAST};</li>
     *   <li>Строим доменную модель {@link QuoteTick}.</li>
     * </ul></p>
     *
     * <p>Примечание: пункты 3) и 4) вынесены в отдельный метод {@link #mapMarketdataToQuoteTick(InstrumentCode, JsonNode)}.</p>
     */
    private Mono<QuoteTick> fetchQuoteOnce(FxSpot instrument) {
        // Примечание: проверка выполняется в AbstractInstrumentHandler, поэтому здесь не требуется

        // 1) Код инструмента --> SECID MOEX ISS
        InstrumentCode domainCode = instrument.instrumentCode();
        String secid = MoexIssFxSpotSupportCatalog.moexSecidOf(domainCode);

        // 2) Запрос к MOEX ISS для получения таблицы marketdata для полученного secid
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/engines/currency/markets/selt/boards/CETS/securities/{secid}.json") // <-- запрос на board CETS
                        .queryParam("iss.meta", "off")
                        .queryParam("iss.only", "marketdata")
                        .queryParam("marketdata.columns", "SYSTIME,LAST")
                        .build(secid)
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).map(body ->
                                new IllegalStateException("MOEX ISS HTTP error " + response.statusCode()
                                        + " for secid=" + secid + ", body=" + body)
                        )
                )
                .bodyToMono(JsonNode.class) // <-- Парсим JSON в дерево JsonNode
                .doOnSubscribe(sub -> log.debug(
                        "Requesting FX_SPOT quote from MOEX ISS: instrumentCode={}, secid={}",
                        domainCode.value(), secid))
                .map(body -> {
                    // 3) Строгая проверка структуры JSON + извлечение SYSTIME/LAST
                    // 4) Построение доменной модели QuoteTick
                    QuoteTick tick = mapMarketdataToQuoteTick(domainCode, body); // <-- Реализация 3) и 4) внутри mapMarketdataToQuoteTick
                    log.debug("Received FX_SPOT QuoteTick from MOEX ISS: {}", tick);
                    return tick;
                });
    }

    /**
     * Строгий маппер блока "marketdata" (JsonNode) в доменную модель QuoteTick.
     *
     * <p>Примечание: SYSTIME приходит без временной зоны. Мы предполагаем что это время {@link #MOEX_ZONE}
     * и переводим в {@link Instant}.</p>
     */
    private QuoteTick mapMarketdataToQuoteTick(InstrumentCode instrumentCode, JsonNode root) {
        /*
         * Ожидаемый JSON-ответ (упрощённо):
         *
         * {
         *   "marketdata": {
         *     "columns": ["SYSTIME", "LAST"],
         *     "data": [
         *       ["2025-03-01 19:00:03", 10.95]
         *     ]
         *   }
         * }
         *
         * Где:
         * - "marketdata" – объект-таблица с данными;
         * - "columns"    – массив имён колонок (порядок важен);
         * - "data"       – массив строк таблицы;
         * - ["2025-03-01 19:00:03", 10.95] – одна строка: [SYSTIME, LAST].
         */

        // 1) Достаём объект "marketdata" и проверяем, что он существует и является объектом
        JsonNode marketdata = root.path("marketdata");
        if (marketdata.isMissingNode() || !marketdata.isObject()) {
            throw new IllegalStateException("MOEX ISS response has no 'marketdata' object");
        }

        // 2) Достаём массивы "columns" и "data" и проверяем, что оба действительно являются массивами
        JsonNode columnsNode = marketdata.path("columns");
        JsonNode dataNode = marketdata.path("data");
        if (!columnsNode.isArray() || !dataNode.isArray()) {
            throw new IllegalStateException("Object 'marketdata' must contain 'columns' and 'data' arrays");
        }

        ArrayNode columns = (ArrayNode) columnsNode;
        ArrayNode data = (ArrayNode) dataNode;

        // 3) Находим индексы колонок "SYSTIME" и "LAST" в массиве "columns"
        int systimeIdx = indexOfColumn(columns, "SYSTIME");
        int lastIdx = indexOfColumn(columns, "LAST");
        if (systimeIdx < 0 || lastIdx < 0) {
            throw new IllegalStateException("Array 'columns' must contain values 'SYSTIME' and 'LAST'");
        }

        // 4) Проверяем, что в массиве "data" ровно одна строка (одна строка в "marketdata" для этого инструмента)
        if (data.size() != 1) {
            throw new IllegalStateException(
                    "Array 'data' must contain exactly one row for instrument " + instrumentCode.value() +
                            ", but was: " + data.size()
            );
        }

        // 5) Извлекаем строку и проверяем, что это массив нужной длины
        JsonNode row = data.get(0);
        if (!row.isArray()) {
            throw new IllegalStateException("Element 0 of 'data' must be an array (marketdata row)");
        }
        if (row.size() <= Math.max(systimeIdx, lastIdx)) {
            throw new IllegalStateException(
                    "Marketdata row has insufficient columns for SYSTIME/LAST"
            );
        }

        // 6) Извлекаем из строки значения, соответствующие колонкам "SYSTIME" и "LAST", проверяем их типы
        JsonNode systimeNode = row.get(systimeIdx);
        JsonNode lastNode = row.get(lastIdx);

        if (systimeNode == null || systimeNode.isNull() || !systimeNode.isTextual()) {
            throw new IllegalStateException("MOEX ISS SYSTIME must be non-null string");
        }
        if (lastNode == null || lastNode.isNull() || !lastNode.isNumber()) {
            throw new IllegalStateException("MOEX ISS LAST must be non-null number");
        }

        // 7) Парсим "SYSTIME" (строка вида "yyyy-MM-dd HH:mm:ss")
        String systimeStr = systimeNode.asText();
        Instant exchangeTs;
        try {
            LocalDateTime ldt = LocalDateTime.parse(systimeStr, MOEX_DATETIME); // <-- Парсим во временную зону MOEX
            exchangeTs = ldt.atZone(MOEX_ZONE).toInstant(); // <-- Конвертируем в Instant (UTC)
        } catch (DateTimeParseException ex) {
            throw new IllegalStateException("Failed to parse MOEX SYSTIME: '" + systimeStr + "'", ex);
        }

        // 8) Парсим "LAST" в BigDecimal
        BigDecimal last = lastNode.decimalValue();

        // 9) Фиксируем время получения тика в нашей системе
        Instant receivedTs = Instant.now();

        // 10) Берём код провайдера из прикреплённого адаптера
        ProviderCode providerCode = provider().providerCode();

        // 11) Собираем итоговый QuoteTick
        return QuoteTick.lastTrade(
                instrumentCode,
                last,
                exchangeTs,
                receivedTs,
                providerCode
        );
    }

    //endregion

    //region UTILS

    /**
     * Поиск индекса колонки по имени в массиве "columns".
     */
    private static int indexOfColumn(ArrayNode columns, String name) {
        for (int i = 0; i < columns.size(); i++) {
            if (name.equalsIgnoreCase(columns.get(i).asText())) {
                return i;
            }
        }
        return -1;
    }

    //endregion
}
