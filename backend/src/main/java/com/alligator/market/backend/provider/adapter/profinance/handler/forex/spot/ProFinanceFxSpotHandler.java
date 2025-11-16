package com.alligator.market.backend.provider.adapter.profinance.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.profinance.ProFinanceAdapter;
import com.alligator.market.backend.provider.adapter.profinance.config.ProFinanceAdapterProps;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.quote.QuoteTick;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Обработчик инструментов FX_SPOT для провайдера рыночных данных ProFinance (парсинг с сайта).
 */
public class ProFinanceFxSpotHandler extends AbstractInstrumentHandler<ProFinanceAdapter, FxSpot> {

    /* Уникальный код обработчика: UPPERCASE, формат [A-Z0-9_]+. */
    private static final String HANDLER_CODE = "PROFINANCE_FX_SPOT_HANDLER";

    /* Поддерживаемые коды инструментов FX_SPOT. */
    private static final Set<String> SUPPORTED_CODES = ProFinanceFxSpotCatalog.SUPPORTED_CODES;

    /* Web-клиент. */
    private final WebClient webClient;

    /* Для логирования. */
    private static final Logger log = LoggerFactory.getLogger(ProFinanceFxSpotHandler.class);

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
     * формируемый периодическими запросами данных методом {@link #fetchOnce(FxSpot)}.
     *
     * <p><b>Поведение:</b></p>
     * <ul>
     *   <li>Поток котировок реализуется путем периодических выполнений метода {@link #fetchOnce(FxSpot)},
     *       который реализует разовый HTTP-запрос к странице провайдера.</li>
     *   <li>Первая попытка выполняется сразу, далее — через каждые {@link ProviderPolicy#minUpdateInterval()}.</li>
     *   <li>Используется стратегия backpressure {@code onBackpressureLatest()} —
     *       если обработка/сеть медленнее интервала, промежуточные «тики» отбрасываются,
     *       обрабатывается только самый свежий.</li>
     *   <li>Сетевые/парсинг-ошибки не завершают поток — тик пропускается ({@code Mono.empty()})
     *       и расписание сохраняется.</li>
     *   <li>Используется {@code concatMap} для гарантии последовательности обработки запросов.</li>
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
                .interval(Duration.ZERO, period) // <-- Интервал запросов
                .onBackpressureLatest() // <-- Если обработка/сеть медленнее интервала, промежуточные «тики» отбрасываются
                .concatMap(t -> fetchOnce(instrument)
                        .onErrorResume(ex -> {
                            // При ошибке парсинга/сети пропускаем тик, ждём следующий интервал
                            log.warn("ProFinance fetch failed: {}", ex.getMessage());
                            return Mono.empty();
                        }))
                .name("profinance-quote");
    }

    //=================================================================================================================
    // ВСПОМОГАТЕЛЬНЫЕ ПРИВАТНЫЕ МЕТОДЫ
    //=================================================================================================================

    /**
     * Выполняет один HTTP-запрос к странице провайдера и преобразует HTML в модель котировки {@link QuoteTick}.
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
                .uri("/quotes/currency/") // Используем относительный путь (baseUrl задан в WebClient)
                .retrieve()
                .bodyToMono(String.class)
                .map(html -> parseHtmlToQuote(html, instrument));
    }

    /**
     * Парсим HTML: ищем строку EUR/USD, читаем Bid/Ask (или Last как fallback).
     */
    private QuoteTick parseHtmlToQuote(String html, FxSpot instrument) {
        Document doc = Jsoup.parse(html);

        // 1) Формируем символ инструмента, характерный для страницы провайдера
        String symbol = instrument.base().code() + "/" + instrument.quote().code(); // <-- Пример: "EUR/USD"

        /* 2) Формируем регулярное выражение (regex) для поиска ячеек (в таблицах на странице провайдера),
              текст внутри которых совпадает с символом инструмента:
              --> ^/$ — якори начала/конца строки;
              --> \s* — допускаем пробелы по краям;
              --> Pattern.quote(symbol) — экранируем символ, чтобы сравнивать его как литерал. */
        String symbolRegex = "^\\s*" + Pattern.quote(symbol) + "\\s*$";

        // 3.1) Используя symbolRegex, ищем ячейки <td> (в таблицах на странице провайдера), содержащие символ инструмента
        Elements cells = doc.select("td:matches(" + symbolRegex + ")");
        if (cells.isEmpty()) {
            throw new IllegalStateException(String.format(
                    "No <td> equal to instrument symbol '%s' found on the page", symbol));
        }
        if (cells.size() > 1) {
            throw new IllegalStateException(String.format(
                    "Ambiguous match: %d <td> with instrument symbol '%s' found on the page", cells.size(), symbol));
        }
        // 3.2) Фиксируем найденную ячейку с символом инструмента, строку данной ячейки и саму таблицу
        Element symbolCell = cells.first(); // <-- пункт 3) гарантирует что symbolCell != null
        Element row = symbolCell != null ? symbolCell.closest("tr") : null;
        Element table = row != null ? row.closest("table") : null;
        if (row == null || table == null) {
            throw new IllegalStateException(String.format(
                    "Wrong table structure: Broken DOM structure near <td> with instrument symbol '%s'", symbol));
        }

        /* 4) Проверяем структуру таблицы:
              --> наличие колонок с заголовками "Name", "Bid", "Ask";
              --> ячейка с символом инструмента должна находиться в колонке "Name". */
        Element header = table.selectFirst("thead tr:has(th), tr:has(th)");
        if (header == null) {
            throw new IllegalStateException("Table header (<th>) not found");
        }
        Elements ths = header.select("th"); // <-- Содержит набор заголовков
        int nameIdx = -1, bidIdx = -1, askIdx = -1; // <-- Начальные значения для индексов
        for (int i = 0; i < ths.size(); i++) {
            // Перебираем заголовки из набора и ищем нужные совпадения
            String h = ths.get(i).text().trim().toLowerCase(Locale.ROOT);
            if (h.equals("name")) nameIdx = i;
            if (h.equals("bid")) bidIdx  = i;
            if (h.equals("ask")) askIdx  = i;
        }
        // Проверка наличия всех трех колонок
        if (nameIdx < 0 || bidIdx < 0 || askIdx < 0) {
            throw new IllegalStateException("Wrong table structure: required columns [Name, Bid, Ask] not found");
        }
        // Индекс найденной ячейки с символом инструмента должен совпадать с индексом колонки "Name"
        Elements tds = row.select("td");
        int cellIdx = tds.indexOf(symbolCell);
        if (cellIdx != nameIdx) {
            throw new IllegalStateException("<td> with instrument symbol is not in the 'Name' column");
        }

        // 5) Извлекаем значения котировок bid/ask
        BigDecimal bid = toDecimal(tds.get(bidIdx).text());
        BigDecimal ask = toDecimal(tds.get(askIdx).text());
        if (bid == null || ask == null) {
            throw new IllegalStateException("Invalid number format in Bid/Ask cells");
        }
        return new QuoteTick(
                instrument.instrumentCode(),
                bid,
                ask,
                Instant.now(),
                provider().providerCode()
        );
    }

    /* Пред-компилированный паттерн: удаляем всё, кроме цифр, запятой, точки, минуса и пробела. */
    private static final Pattern NON_NUMERIC = Pattern.compile("[^\\d.,\\- ]");

    /**
     * Нормализуем число из ячейки.
     */
    private static BigDecimal toDecimal(String raw) {
        if (raw == null) return null;

        // 1) "Очищаем" строку:
        String s = raw
                // 1) Заменяем NBSP и узкие пробелы на обычный
                .replace('\u00A0', ' ')
                .replace('\u202F', ' ')
                .replace('\u2009', ' ')
                // 2) Используем пред-компилированный паттерн
                .replaceAll(NON_NUMERIC.pattern(), "")
                .replace(" ", "")
                // 3) Заменяем запятую на точку
                .replace(',', '.');

        // 2) Оставляем только последнюю точку как десятичный разделитель (остальные — тысячные) // TODO усилить проверку - нет точек (целое число) или только одна точка
        int dot = s.lastIndexOf('.');
        if (dot > 0) s = s.substring(0, dot).replace(".", "") + s.substring(dot);

        // 3) Проверка на пустоту или прочерк
        if (s.isEmpty() || s.equals("-")) return null;

        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) { // TODO добавить выбрасывание ошибки
            return null;
        }
    }
}
