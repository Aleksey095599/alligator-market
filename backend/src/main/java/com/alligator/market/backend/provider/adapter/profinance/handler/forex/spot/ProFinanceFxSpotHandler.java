package com.alligator.market.backend.provider.adapter.profinance.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.profinance.ProFinanceAdapter;
import com.alligator.market.backend.provider.adapter.profinance.config.ProFinanceAdapterProps;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.code.InstrumentCode;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/**
 * Обработчик инструментов FX_SPOT для провайдера рыночных данных ProFinance (парсинг с сайта).
 */
@Slf4j
public class ProFinanceFxSpotHandler extends AbstractInstrumentHandler<ProFinanceAdapter, FxSpot> {

    /* Уникальный код обработчика: UPPERCASE, формат [A-Z0-9_]+. */
    private static final String HANDLER_CODE = "PROFINANCE_FX_SPOT_HANDLER";

    /* Поддерживаемые коды инструментов FX_SPOT. */
    private static final Set<InstrumentCode> SUPPORTED_CODES = ProFinanceFxSpotCatalog.SUPPORTED_CODES;

    /* Web-клиент. */
    private final WebClient webClient;

    //=================================================================================================================
    // КОНСТРУКТОР
    //=================================================================================================================

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
    }

    //=================================================================================================================
    // РЕАЛИЗАЦИЯ МЕТОДОВ КОНТРАКТА
    //=================================================================================================================

    /**
     * Возвращает реактивный поток котировок для указанного FX-спот инструмента,
     * формируемый периодическими одиночными запросами котировки методом {@link #fetchOnce(FxSpot)}.
     *
     * <p><b>Поведение:</b></p>
     * <ul>
     *   <li>Поток котировок реализуется путем периодических выполнений метода {@link #fetchOnce(FxSpot)},
     *       который реализует разовый HTTP-запрос к странице провайдера.</li>
     *   <li>Первая попытка выполняется сразу, далее – через каждые {@link ProviderPolicy#minUpdateInterval()}.</li>
     *   <li>Используется стратегия backpressure {@code onBackpressureLatest()} –
     *       если обработка/сеть медленнее интервала, промежуточные «тики» отбрасываются,
     *       обрабатывается только самый свежий.</li>
     *   <li>Сетевые/парсинг-ошибки не завершают поток – тик пропускается ({@code Mono.empty()})
     *       и расписание сохраняется.</li>
     *   <li>Используется {@code switchMap} чтобы "запаздывающий" ответ не попадал в поток.</li>
     * </ul>
     *
     * @param instrument FX_SPOT инструмент
     * @return поток {@link QuoteTick}
     */
    @Override
    protected Publisher<QuoteTick> doQuote(FxSpot instrument) {
        /* Извлекаем ограничение провайдера на минимальный интервал между запросами котировки.
           Этот интервал и будет использоваться как период запросов данных. */
        Duration period = provider().policy().minUpdateInterval();

        return Flux
                .interval(Duration.ZERO, period) // <-- Первый запрос стразу, следующие с интервалом period
                .onBackpressureLatest() // <-- Если обработка/сеть медленнее интервала, промежуточные «тики» отбрасываются
                .switchMap(t -> fetchOnce(instrument)
                        .timeout(period)
                        .onErrorResume(ex -> {
                            // При ошибке парсинга/сети пропускаем тик, ждём следующий интервал
                            log.warn("ProFinance fetch failed: {}", ex.getMessage());
                            return Mono.empty();
                        }))
                .name("profinance-quote");
    }

    //=================================================================================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    //=================================================================================================================

    /**
     * Метод реализует одиночный запрос котировки: выполняет один HTTP-запрос к странице провайдера и преобразует
     * HTML в модель котировки {@link QuoteTick}.
     *
     * <p>Делает GET на относительный путь {@code /quotes/currency/} (baseUrl задан в WebClient),
     * получает HTML как {@code String} и далее применяет метод {@link #parseHtmlToQuote(String, FxSpot)} для поиска
     * нужного числового значения котировки и преобразования его в модель котировки.</p>
     *
     * <p>Ошибки сети/парсинга не обрабатываются здесь и пробрасываются вверх
     * (деградация выполняется в вызывающем коде).</p>
     *
     * @param instrument FX-SPOT инструмент
     * @return {@code Mono} c единичным {@link QuoteTick}
     */
    private Mono<QuoteTick> fetchOnce(FxSpot instrument) {

        return webClient.get()
                .uri("/quotes/") // Используем относительный путь (baseUrl задан в WebClient)
                .retrieve()
                .bodyToMono(String.class)
                .map(html -> parseHtmlToQuote(html, instrument));
    }

    /**
     * Парсим HTML: ищем строку с символом заданного инструмента и читаем значения Bid/Ask.
     */
    private QuoteTick parseHtmlToQuote(String html, FxSpot instrument) {
        // Разбираем HTML-строку в DOM-модель Jsoup, чтобы далее искать элементы таблиц/ячеек
        Document doc = Jsoup.parse(html);

        // 1) Формируем символ инструмента, характерный для страницы провайдера
        String symbol = instrument.base().code().value() + "/" + instrument.quote().code().value(); // <-- Пример: "EUR/USD"

        /* 2) Формируем регулярное выражение (regex) для поиска нужной ячейки в таблицах на странице провайдера,
              текст внутри которых совпадает с символом инструмента:
              --> ^/$ – якори начала/конца строки;
              --> \s* – допускаем пробелы по краям;
              --> Pattern.quote(symbol) – экранируем символ, чтобы сравнивать его как литерал. */
        String symbolRegex = "^\\s*" + java.util.regex.Pattern.quote(symbol) + "\\s*$";

        // 3.1) Ищем в таблицах на странице провайдера ячейки, содержащие символ инструмента (матч по symbolRegex)
        Elements tds = doc.select("td:matches(" + symbolRegex + ")");
        if (tds.isEmpty()) {
            throw new IllegalStateException(String.format(
                    // TODO: данная ошибка может трактоваться как неподдерживаемый инструмент, возможно стоит заменить на специальную доменную ошибку (подумать как сделать корректно)
                    "No <td> equal to instrument symbol '%s' found on the page", symbol));
        }
        if (tds.size() > 1) {
            throw new IllegalStateException(String.format(
                    "Ambiguous match: %d <td> with instrument symbol '%s' found on the page " +
                            "(expected only one)", tds.size(), symbol));
        }
        // 3.2) Фиксируем найденную ячейку, строку ячейки и таблицу, в которой найдена ячейка
        Element symbolCell = tds.first(); // <-- точно не null, согласно проверкам в 3.1
        Element symbolRow = symbolCell != null ? symbolCell.closest("tr") : null;
        Element symbolTable = symbolRow != null ? symbolRow.closest("table") : null;
        if (symbolRow == null || symbolTable == null) {
            throw new IllegalStateException(String.format(
                    // Если элементы symbolRow или symbolTable ==null --> некорректная структура вокруг найденной ячейки
                    "Wrong structure of table with instrument symbol: " +
                            "Broken DOM structure near <td> with instrument symbol '%s'", symbol));
        }

        /* 4) Проверяем структуру таблицы:
              <ul>
                <li>4.1 наличие заголовков "Name", "Bid" и "Ask" в symbolTable;</li>
                <li>4.2 symbolCell должна находиться под заголовком "Name";</li>
                <li>4.3 symbolRow содержит столько же ячеек сколько и строка заголовков.</li>
              </ul> */
        Element headerRow = symbolTable.selectFirst("thead tr:has(th), tr:has(th)");
        if (headerRow == null) {
            throw new IllegalStateException("Row with headers (<th>) not found in the table with " +
                    "instrument symbol: " + symbol);
        }
        Elements headerCells = headerRow.select("th"); // <-- Набор ячеек headerRow
        int nameIdx = -1, bidIdx = -1, askIdx = -1; // <-- Начальные значения для индексов искомых ячеек в headerRow
        for (int i = 0; i < headerCells.size(); i++) {
            // Перебираем ячейки в headerRow и ищем нужные совпадения ("name", "bid", "ask")
            String h = headerCells.get(i).text().trim().toLowerCase(Locale.ROOT); // <-- Значение i-ой ячейки в headerRow
            if (h.equals("name")) nameIdx = i;
            if (h.equals("bid")) bidIdx = i;
            if (h.equals("ask")) askIdx = i;
        }
        // 4.1 Проверка наличия всех трех нужных заголовков
        if (nameIdx < 0 || bidIdx < 0 || askIdx < 0) {
            throw new IllegalStateException("Wrong structure of table with instrument symbol: " +
                    "required columns [Name, Bid, Ask] not found");
        }
        // 4.2 Индекс symbolCell должен совпадать с индексом ячейки с заголовком "Name"
        Elements symbolCells = symbolRow.select("td"); // <-- Набор ячеек в symbolRow
        int cellIdx = symbolCells.indexOf(symbolCell);
        if (cellIdx != nameIdx) {
            throw new IllegalStateException("<td> with instrument symbol is not in the 'Name' column");
        }
        // 4.3 symbolRow содержит столько же ячеек сколько и headerRow
        if (symbolCells.size() != headerCells.size()) {
            throw new IllegalStateException(
                    "Wrong structure of table with instrument symbol: header has " + headerCells.size()
                            + " columns but row with instrument symbol has " + symbolCells.size() + " <td>"
            );
        }

        // 5) Извлекаем значения котировок bid/ask
        BigDecimal bid = toDecimal(symbolCells.get(bidIdx).text());
        BigDecimal ask = toDecimal(symbolCells.get(askIdx).text());

        // Семантическая проверка значений bid/ask
        if (bid.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Bid must be > 0 for instrument '" + symbol + "'");
        }
        if (ask.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Ask must be > 0 for instrument '" + symbol + "'");
        }
        if (ask.compareTo(bid) <= 0) {
            throw new IllegalStateException("Ask must be greater than Bid for instrument '" + symbol +
                    "': bid=" + bid + ", ask=" + ask);
        }

        // 6) Формируем и возвращаем модель котировки
        return new QuoteTick(
                instrument.instrumentCode(),
                null,
                bid,
                ask,
                Instant.now(),
                Instant.now(),
                provider().providerCode()
        );
    }

    //=================================================================================================================
    // УТИЛИТЫ
    //=================================================================================================================

    /* Пред-компилированный паттерн: разрешены только цифры и опциональная десятичная часть через точку. */
    private static final java.util.regex.Pattern DECIMAL_DOT_NO_SIGN =
            java.util.regex.Pattern.compile("\\d+(\\.\\d+)?");

    /* Разные типы минусов, которые встречаются в HTML (ASCII и настоящий Unicode minus). */
    private static final char ASCII_MINUS = '-';
    private static final char UNICODE_MINUS = '\u2212';

    /**
     * Нормализует текст ячейки и парсит BigDecimal по строгим правилам:
     * - запятая недопустима;
     * - допускается одна точка (как десятичный разделитель) или ни одной точки (целое число);
     * - отрицательные значения недопустимы.
     */
    private static BigDecimal toDecimal(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("Input value for decimal parsing is null");
        }

        // 1) "Очищаем" строку:
        String normalized = raw
                // Заменяем NBSP, узкие и тонкие пробелы на обычные
                .replace('\u00A0', ' ')
                .replace('\u202F', ' ')
                .replace('\u2009', ' ')
                // Убираем обычные пробелы
                .replace(" ", "");
        // 2) Запятая недопустима
        if (normalized.indexOf(',') >= 0) {
            throw new IllegalArgumentException("Decimal comma is not allowed: '" + raw + "' --> '" + normalized + "'");
        }
        // 3) Минусы недопустимы (включая ASCII hyphen-minus и Unicode minus)
        if (normalized.indexOf(ASCII_MINUS) >= 0 || normalized.indexOf(UNICODE_MINUS) >= 0) {
            throw new IllegalArgumentException("Negative value is not allowed: '" + raw + "' --> '" + normalized + "'");
        }
        // 4) Не более одной точки
        int firstDot = normalized.indexOf('.');
        if (firstDot != -1 && normalized.indexOf('.', firstDot + 1) != -1) {
            throw new IllegalArgumentException("More than one dot: '" + raw + "' --> '" + normalized + "'");
        }
        // 5) Финальная валидация формата с помощью паттерна
        if (!DECIMAL_DOT_NO_SIGN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid numeric format: '" + raw + "' --> '" + normalized + "'");
        }

        // Пробуем преобразовать в число
        try {
            return new BigDecimal(normalized);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot parse BigDecimal: '" + raw + "' --> '" + normalized + "'", e);
        }
    }
}
