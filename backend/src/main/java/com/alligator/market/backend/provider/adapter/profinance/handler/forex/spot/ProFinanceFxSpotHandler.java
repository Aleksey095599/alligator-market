package com.alligator.market.backend.provider.adapter.profinance.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.profinance.ProFinanceAdapter;
import com.alligator.market.backend.provider.adapter.profinance.config.ProFinanceAdapterProps;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
     * формируемый периодическим опросом источника.
     *
     * <p><b>Поведение:</b></p>
     * <ul>
     *   <li>Первая попытка выполняется сразу, далее — каждые
     *       {@code provider().policy().minUpdateInterval()} (см. {@link reactor.core.publisher.Flux#interval}).</li>
     *   <li>Используется стратегия backpressure {@code onBackpressureLatest()} —
     *       если обработка/сеть медленнее интервала, промежуточные «тики» отбрасываются,
     *       обрабатывается только самый свежий.</li>
     *   <li>Сетевые/парсинг-ошибки не завершают поток: тик пропускается
     *       ({@code Mono.empty()}) и расписание сохраняется.</li>
     *   <li>Каждый тик инициирует один HTTP-запрос в {@code fetchOnce(instrument)} и маппится в {@link QuoteTick}.</li>
     * </ul>
     *
     * <p><b>Примечание:</b> при необходимости полностью исключить параллельные запросы
     * используйте ограничение конкурентности {@code flatMap(..., 1)}.</p>
     *
     * @param instrument FX_SPOT инструмент
     * @return поток {@link QuoteTick} с указанным интервалом; устойчив к временным ошибкам источника
     */
    @Override
    protected Publisher<QuoteTick> doQuote(FxSpot instrument) {
        // Извлекаем ограничение провайдера на минимальный интервал между запросами котировки
        Duration period = provider().policy().minUpdateInterval();

        return Flux
                .interval(Duration.ZERO, period) // Интервал для запросов [0, period]
                .onBackpressureLatest() // Если обработка/сеть медленнее интервала, промежуточные «тики» отбрасываются
                .flatMap(t -> fetchOnce(instrument)
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
     * Выполняет один HTTP-запрос к странице валют и преобразует HTML в модель котировки {@link QuoteTick}.
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

        // Формируем символ инструмента, характерный для сайта провайдера
        String symbol = instrument.base().code() + "/" + instrument.quote().code(); // --> Например, "EUR/USD"

        /* Формируем regex (регулярное выражение) для точного совпадения текста ячейки с символом инструмента:
           ^/$ — якори начала/конца строки; \s* — допускаем пробелы по краям;
           Pattern.quote(symbol) — экранируем символ, чтобы сравнивать его как литерал. */
        String symbolRegex = "^\\s*" + Pattern.quote(symbol) + "\\s*$";

        /* Ищем на всей странице во всех таблицах <tr> (table row), у которого есть <td> (table data) совпадающий
           с symbolRegex (берем первый же, который встретится). Если не нашли, выбрасываем исключение.
         */
        Element row = doc.selectFirst("tr:has(td:matches(" + symbolRegex + "))");
        if (row == null) {
            throw new IllegalStateException(
                    String.format("No table row (<tr>) with a cell (<td>) equal to '%s' found on the page", symbol)
            );
        }

        // Пытаемся найти индексы колонок, в которых будут содержаться значения цен bid и ask
        int bidIdx = -1, askIdx = -1, lastIdx = -1;
        Element table = row.closest("table");
        if (table != null) {
            Element header = table.selectFirst("tr:has(th)");
            if (header != null) {
                int i = 0;
                for (Element th : header.select("th")) {
                    String h = th.text().trim().toLowerCase(Locale.ROOT);
                    if (h.contains("bid") || h.contains("покуп")) bidIdx = i;
                    if (h.contains("ask") || h.contains("прод")) askIdx = i;
                    if (h.contains("last") || h.contains("посл") || h.contains("цена")) lastIdx = i;
                    i++;
                }
            }
        }

        var tds = row.select("td");
        if (tds.isEmpty()) {
            throw new IllegalStateException("Unexpected table row format: no <td> cells");
        }

        BigDecimal bid;
        BigDecimal ask;

        // 1) Если распознали Bid/Ask по заголовкам — берём их
        if (bidIdx >= 0 && askIdx >= 0 && bidIdx < tds.size() && askIdx < tds.size()) {
            bid = toDecimal(tds.get(bidIdx).text());
            ask = toDecimal(tds.get(askIdx).text());
        }
        // 2) Иначе пытаемся по дефолтной схеме (0=Type, 1=Bid, 2=Ask)
        else if (tds.size() >= 3) {
            bid = toDecimal(tds.get(1).text());
            ask = toDecimal(tds.get(2).text());
        }
        // 3) Fallback: только Last — берём как mid и дублируем в bid/ask
        else if (lastIdx >= 0 && lastIdx < tds.size()) {
            BigDecimal last = toDecimal(tds.get(lastIdx).text());
            bid = last;
            ask = last;
        } else if (tds.size() >= 2) { // иногда бывает Type+Last
            BigDecimal last = toDecimal(tds.get(1).text());
            bid = last;
            ask = last;
        } else {
            throw new IllegalStateException("Unable to detect Bid/Ask/Last columns");
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

        // "Очищаем" строку:
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

        // Оставляем только последнюю точку как десятичный разделитель (остальные — тысячные)
        int dot = s.lastIndexOf('.');
        if (dot > 0) s = s.substring(0, dot).replace(".", "") + s.substring(dot);

        // Проверка на пустоту или прочерк
        if (s.isEmpty() || s.equals("-")) return null;

        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
