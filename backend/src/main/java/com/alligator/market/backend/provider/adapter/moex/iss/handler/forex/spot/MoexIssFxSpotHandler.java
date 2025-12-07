package com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssAdapter;
import com.alligator.market.backend.provider.adapter.moex.iss.config.MoexIssAdapterProps;
import com.alligator.market.backend.provider.adapter.moex.iss.config.MoexIssWebConfig;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Set;

/**
 * Обработчик инструментов FX_SPOT для провайдера MOEX ISS.
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

    /* Формат даты/времени поля SYSTIME в ответе MOEX ISS. */
    private static final DateTimeFormatter MOEX_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /* Временная зона MOEX (используется для конвертации SYSTIME в Instant). */
    private static final ZoneId MOEX_ZONE = ZoneId.of("Europe/Moscow");

    //=================================================================================================================
    // КОНСТРУКТОР
    //=================================================================================================================

    /**
     * Конструктор обработчика.
     *
     * @param props     параметры подключения к провайдеру {@link MoexIssAdapterProps}
     * @param webClient web-клиент, настроенный для данного провайдера {@link MoexIssWebConfig}
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
     * Чистая логика получения котировки FX_SPOT с MOEX ISS.
     *
     * <p>Алгоритм:
     * <ul>
     *   <li>1) Доменный код инструмента конвертируем в SECID, который ожидает MOEX ISS;</li>
     *   <li>2) Запрашиваем у MOEX ISS таблицу {@code marketdata} для board CETS и нужного SECID;</li>
     *   <li>3) Строго проверяем структуру JSON и извлекаем {@code SYSTIME} и {@code LAST};</li>
     *   <li>4) Строим доменную модель {@link QuoteTick}.</li>
     * </ul>
     *
     * <p>Примечание: пункты 3) и 4) вынесены в отдельный метод {@link #mapMarketdataToQuoteTick(String, JsonNode)}.
     */
    @Override
    protected Publisher<QuoteTick> doQuote(FxSpot instrument) {
        // Примечание: проверка инструмента здесь не требуется, она выполняется в AbstractInstrumentHandler.

        // Доменный код инструмента
        String domainCode = instrument.instrumentCode();

        // Конвертируем доменный код в SECID MOEX ISS
        String secid = MoexIssFxSpotInstruments.moexSecidOf(domainCode);

        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/engines/currency/markets/selt/boards/CETS/securities/{secid}.json")
                        .queryParam("iss.meta", "off")
                        .queryParam("iss.only", "marketdata")
                        .queryParam("marketdata.columns", "SYSTIME,LAST")
                        .build(secid)
                )
                .retrieve()
                .bodyToMono(JsonNode.class) // <-- Парсим JSON в дерево JsonNode
                .doOnSubscribe(sub -> log.debug(
                        "Requesting FX_SPOT quote from MOEX ISS: instrumentCode={}, secid={}",
                        domainCode, secid))
                .doOnNext(body -> {
                    // ВРЕМЕННАЯ отладка в консоль (TODO: после отладки убрать)
                    System.out.println("=== MOEX ISS RAW RESPONSE ===");
                    System.out.println("instrumentCode = " + domainCode + ", secid = " + secid);
                    System.out.println(body);
                    System.out.println("=== END OF RESPONSE ===");
                })
                // Преобразуем JsonNode --> QuoteTick
                .map(body -> mapMarketdataToQuoteTick(domainCode, body));
    }

    //=================================================================================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    //=================================================================================================================

    /*
     * Строгий маппер блока "marketdata" (JsonNode) в доменную модель QuoteTick.
     */
    private QuoteTick mapMarketdataToQuoteTick(String instrumentCode, JsonNode root) {
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
         * - "marketdata" — объект-таблица с данными;
         * - "columns"    — массив имён колонок (порядок важен);
         * - "data"       — массив строк таблицы;
         * - ["2025-03-01 19:00:03", 10.95] — одна строка: [SYSTIME, LAST].
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
                    "Array 'data' must contain exactly one row for instrument " + instrumentCode +
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

        // 7) Парсим "SYSTIME" (строка вида "yyyy-MM-dd HH:mm:ss") в Instant с учётом временной зоны MOEX <-- TODO: разобраться со временем
        String systimeStr = systimeNode.asText();
        Instant exchangeTs;
        try {
            LocalDateTime ldt = LocalDateTime.parse(systimeStr, MOEX_DATETIME);
            exchangeTs = ldt.atZone(MOEX_ZONE).toInstant();
        } catch (DateTimeParseException ex) {
            throw new IllegalStateException("Failed to parse MOEX SYSTIME: '" + systimeStr + "'", ex);
        }

        // 8) Парсим "LAST" в BigDecimal
        BigDecimal last = lastNode.decimalValue();

        // 9) Фиксируем время получения тика в нашей системе
        Instant receivedTs = Instant.now();

        // 10) Берём код провайдера из прикреплённого адаптера
        String providerCode = provider().providerCode();

        // 11) Собираем итоговый QuoteTick
        return QuoteTick.lastTrade(
                instrumentCode,
                last,
                exchangeTs,
                receivedTs,
                providerCode
        );
    }

    //=================================================================================================================
    // УТИЛИТЫ
    //=================================================================================================================

    /*
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
}
