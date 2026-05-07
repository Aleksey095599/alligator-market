package com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.handler;

import com.alligator.market.backend.source.adapter.moex.iss.MoexIssMarketDataSource;
import com.alligator.market.backend.source.adapter.moex.iss.instrument.forex.spot.support.MoexIssFxSpotSupportCatalog;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.marketdata.tick.level.source.type.SourceLastPriceTick;
import com.alligator.market.domain.marketdata.tick.level.source.vo.SourceInstrumentCode;
import com.alligator.market.domain.source.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.source.passport.classification.AccessMethod;
import com.alligator.market.domain.source.vo.HandlerCode;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ArrayNode;

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
 * Обработчик инструментов FOREX_SPOT source MOEX ISS {@link MoexIssMarketDataSource}.
 */
@Slf4j
public class MoexIssFxSpotHandler extends AbstractInstrumentHandler<MoexIssMarketDataSource, FxSpot> {

    /* Код обработчика. */
    private static final HandlerCode HANDLER_CODE = HandlerCode.of("MOEX_ISS_FX_SPOT_HANDLER");

    /* Поддерживаемые инструменты FOREX_SPOT. */
    private static final Set<FxSpot> SUPPORTED_INSTRUMENTS = MoexIssFxSpotSupportCatalog.SUPPORTED_INSTRUMENTS;

    /* Web-клиент. */
    private final WebClient webClient;

    /* Формат даты/времени поля SYSTIME в ответе MOEX ISS. */
    private static final DateTimeFormatter MOEX_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /* Временная зона MOEX (используется для конвертации SYSTIME в Instant). */
    private static final ZoneId MOEX_ZONE = ZoneId.of("Europe/Moscow");

    /**
     * Конструктор обработчика.
     *
     * @param webClient web-клиент, настроенный для запросов провайдеру MOEX ISS по инструментам FOREX_SPOT
     */
    public MoexIssFxSpotHandler(WebClient webClient) {
        super(HANDLER_CODE, FxSpot.class, SUPPORTED_INSTRUMENTS);

        Objects.requireNonNull(webClient, "webClient must not be null");
        this.webClient = webClient;
    }

    /**
     * Streams source-level ticks for the given FOREX_SPOT instrument.
     *
     * <p>Implements the {@link AccessMethod#API_POLL} access method:
     * one request --> one tick --> provider policy delay --> repeat.</p>
     */
    @Override
    protected Publisher<SourceMarketDataTick> doStreamSourceTicks(FxSpot instrument) {
        // 1) Получаем минимальный интервал обновления из "политики" провайдера
        Duration pollInterval = provider().policy().minUpdateInterval();

        // Далее в виде цепочки:
        // 2) Запрашиваем один source tick
        // 3) Ошибки не “убивают” поток: логируем и пропускаем тик
        // 4) Повторяем с задержкой согласно "политике" провайдера
        return fetchSourceTickOnce(instrument)
                .onErrorResume(ex -> {
                    log.warn(
                            "Failed to fetch FX_SPOT source tick from MOEX ISS: instrumentCode={}, reason={}",
                            instrument.instrumentCode().value(),
                            ex.getMessage(),
                            ex
                    );
                    return Mono.empty();
                })
                .repeatWhen(completed -> completed.delayElements(pollInterval));
    }

    /**
     * Один запрос к MOEX ISS --> один source tick инструмента FOREX_SPOT.
     *
     * <p>Алгоритм:
     * <ul>
     *   <li>Код инструмента конвертируем в SECID, который ожидает MOEX ISS;</li>
     *   <li>Запрашиваем у MOEX ISS таблицу {@code marketdata} с рыночными данными для SECID;</li>
     *   <li>Строго проверяем полученную JSON-структуру и извлекаем {@code SYSTIME} и {@code LAST};</li>
     *   <li>Строим source-level рыночный тик {@link SourceMarketDataTick}.</li>
     * </ul></p>
     *
     * <p>Примечания:
     * <ul>
     *     <li>Под таблицей {@code marketdata} подразумевается формат передачи рыночных, описанный в документации
     *     провайдера MOEX ISS;</li>
     *     <li>3-й и 4-й пункты вынесены в отдельный метод.</li>
     * </ul>
     */
    private Mono<SourceMarketDataTick> fetchSourceTickOnce(FxSpot instrument) {
        // Примечание: проверка выполняется в AbstractInstrumentHandler, поэтому здесь не требуется

        // 1) Код инструмента --> SECID MOEX ISS
        InstrumentCode domainCode = instrument.instrumentCode();
        SourceInstrumentCode secid = MoexIssFxSpotSupportCatalog.moexSecidOf(domainCode);

        // 2) Запрос к MOEX ISS для получения таблицы marketdata для полученного secid
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/engines/currency/markets/selt/boards/CETS/securities/{secid}.json") // <-- запрос на board CETS
                        .queryParam("iss.meta", "off")
                        .queryParam("iss.only", "marketdata")
                        .queryParam("marketdata.columns", "SYSTIME,LAST")
                        .build(secid.value())
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class).map(body ->
                                new IllegalStateException("MOEX ISS HTTP error " + response.statusCode()
                                        + " for secid=" + secid.value() + ", body=" + body)
                        )
                )
                .bodyToMono(JsonNode.class) // <-- Парсим JSON в дерево JsonNode
                .doOnSubscribe(sub -> log.debug(
                        "Requesting FX_SPOT source tick from MOEX ISS: instrumentCode={}, secid={}",
                        domainCode.value(), secid.value()))
                .map(body -> {
                    // 3) Строгая проверка структуры JSON + извлечение SYSTIME/LAST
                    // 4) Построение source-level тика
                    SourceMarketDataTick tick = mapMarketdataToSourceTick(secid, body);
                    log.debug("Received FX_SPOT SourceMarketDataTick from MOEX ISS: {}", tick);
                    return tick;
                });
    }

    /**
     * Строгий маппер блока "marketdata" (JsonNode) в source-level рыночный тик.
     *
     * <p>Примечание: SYSTIME приходит без временной зоны. Мы предполагаем что это время {@link #MOEX_ZONE}
     * и переводим в {@link Instant}.</p>
     */
    private SourceMarketDataTick mapMarketdataToSourceTick(SourceInstrumentCode secid, JsonNode root) {
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
         * "marketdata" – объект-таблица с данными;
         * "columns" – массив имён колонок (порядок важен);
         * "data" – массив строк таблицы;
         * ["2025-03-01 19:00:03", 10.95] – одна строка: [SYSTIME, LAST].
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
                    "Array 'data' must contain exactly one row for source instrument " + secid.value() +
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

        if (systimeNode == null || systimeNode.isNull() || !systimeNode.isString()) {
            throw new IllegalStateException("MOEX ISS SYSTIME must be non-null string");
        }
        if (lastNode == null || lastNode.isNull() || !lastNode.isNumber()) {
            throw new IllegalStateException("MOEX ISS LAST must be non-null number");
        }

        // 7) Парсим "SYSTIME" (строка вида "yyyy-MM-dd HH:mm:ss")
        String systimeStr = systimeNode.stringValue();

        Instant sourceTimestamp;
        try {
            LocalDateTime ldt = LocalDateTime.parse(systimeStr, MOEX_DATETIME); // <-- Парсим во временную зону MOEX
            sourceTimestamp = ldt.atZone(MOEX_ZONE).toInstant(); // <-- Конвертируем в Instant (UTC)
        } catch (DateTimeParseException ex) {
            throw new IllegalStateException("Failed to parse MOEX SYSTIME: '" + systimeStr + "'", ex);
        }

        // 8) Парсим "LAST" в BigDecimal
        BigDecimal last = lastNode.decimalValue();

        // 9) Собираем source-level last price тик
        return new SourceLastPriceTick(
                secid,
                last,
                sourceTimestamp
        );
    }

    /**
     * Поиск индекса колонки по имени в массиве "columns".
     */
    private static int indexOfColumn(ArrayNode columns, String name) {
        for (int i = 0; i < columns.size(); i++) {
            JsonNode columnNode = columns.get(i);

            // Ищем только строковые имена колонок, без scalar-coercion.
            if (!columnNode.isString()) {
                continue;
            }

            String columnName = columnNode.stringValue();
            if (name.equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }
}
