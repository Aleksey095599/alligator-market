package com.alligator.market.backend.provider.adapter.profinance.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.profinance.ProFinanceAdapter;
import com.alligator.market.backend.provider.adapter.profinance.config.ProFinanceAdapterProps;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotValueDate;
import com.alligator.market.domain.quote.QuoteTick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Юнит‑тесты для parseHtmlToQuote(...) в ProFinanceFxSpotHandler.
 * Тесты не ходят на реальный сайт — используем HTML-фикстуры как строки.
 */
class ProFinanceFxSpotHandlerParseHtmlToQuoteTest {

    /* Тестируемый обработчик. */
    private ProFinanceFxSpotHandler handler;

    /* Тестовый инструмент EUR/USD. */
    private FxSpot eurUsd;

    @BeforeEach
    void setUp() throws Exception {
        // 1) Создаём простой WebClient с фейковым baseUrl (в этих тестах он не используется).
        WebClient webClient = WebClient.builder()
                .baseUrl("https://example.test")
                .build();

        // 2) Создаём экземпляр хендлера
        handler = new ProFinanceFxSpotHandler(webClient, new ProFinanceAdapterProps("https://example.test"));

        /*
         * 3) Прикрепляем к хендлеру реальный провайдер ProFinanceAdapter.
         *    Mockito не умеет мокать sealed-интерфейс MarketDataProvider в текущей конфигурации
         *    (mockito-inline даёт ошибку Unsupported settings with this type ...), поэтому
         *    используем настоящий адаптер с тестовыми параметрами и прикрепляем его к хендлеру
         *    штатным методом attachTo().
         */
        ProFinanceAdapter provider = new ProFinanceAdapter(
                new ProFinanceAdapterProps("https://example.test"),
                webClient
        );
        handler.attachTo(provider);

        // 4) Готовим реальный FxSpot для EUR/USD
        Currency eur = new Currency(CurrencyCode.of("EUR"), "Euro", "European Union", 2);
        Currency usd = new Currency(CurrencyCode.of("USD"), "United States Dollar", "United States", 2);
        eurUsd = new FxSpot(eur, usd, FxSpotValueDate.TOM, 4);
        // instrumentType здесь не используется, но можно проставить для консистентности:
        assertEquals(InstrumentType.FX_SPOT, eurUsd.instrumentType());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ПОЗИТИВНЫЙ КЕЙС
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void parseHtml_happyPath_eurUsd() {
        // 1) HTML-фикстура: одна таблица, заголовок Name|Bid|Ask и строка EUR/USD
        String html = """
                <html><body>
                  <table>
                    <thead>
                      <tr><th>Name</th><th>Bid</th><th>Ask</th></tr>
                    </thead>
                    <tbody>
                      <tr><td>EUR/USD</td><td>1.0710</td><td>1.0712</td></tr>
                    </tbody>
                  </table>
                </body></html>
                """;

        // 2) Вызываем приватный метод через reflection
        QuoteTick tick = invokeParseHtmlToQuote(handler, html, eurUsd);

        // 3) Проверяем, что поля котировки соответствуют ожиданиям
        assertEquals(eurUsd.instrumentCode(), tick.instrumentCode());
        assertEquals(new BigDecimal("1.0710"), tick.bid());
        assertEquals(new BigDecimal("1.0712"), tick.ask());
        assertEquals("PROFINANCE", tick.providerCode());
        assertFalse(tick.timestamp().isAfter(Instant.now()));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // НЕГАТИВНЫЕ КЕЙСЫ С ОЖИДАЕМЫМИ ОШИБКАМИ
    // -----------------------------------------------------------------------------------------------------------------

    @Test
    void parseHtml_noTdWithSymbol_throws() {
        // Страница без EUR/USD
        String html = """
                <html><body>
                  <table>
                    <thead>
                      <tr><th>Name</th><th>Bid</th><th>Ask</th></tr>
                    </thead>
                    <tbody>
                      <tr><td>GBP/USD</td><td>1.2310</td><td>1.2312</td></tr>
                    </tbody>
                  </table>
                </body></html>
                """;

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> invokeParseHtmlToQuote(handler, html, eurUsd)
        );

        assertTrue(ex.getMessage().contains("No <td> equal to instrument symbol 'EUR/USD'"),
                "Unexpected error message: " + ex.getMessage());
    }

    @Test
    void parseHtml_ambiguousSymbol_throws() {
        // Две ячейки EUR/USD (в одной или нескольких таблицах) → неоднозначность
        String html = """
                <html><body>
                  <table>
                    <thead>
                      <tr><th>Name</th><th>Bid</th><th>Ask</th></tr>
                    </thead>
                    <tbody>
                      <tr><td>EUR/USD</td><td>1.0710</td><td>1.0712</td></tr>
                    </tbody>
                  </table>
                  <table>
                    <thead>
                      <tr><th>Name</th><th>Bid</th><th>Ask</th></tr>
                    </thead>
                    <tbody>
                      <tr><td>EUR/USD</td><td>1.0709</td><td>1.0711</td></tr>
                    </tbody>
                  </table>
                </body></html>
                """;

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> invokeParseHtmlToQuote(handler, html, eurUsd)
        );

        assertTrue(ex.getMessage().contains("Ambiguous match"),
                "Unexpected error message: " + ex.getMessage());
    }

    @Test
    void parseHtml_missingRequiredColumns_throws() {
        // В заголовке нет колонки Name → 4.1 должен отработать
        String html = """
                <html><body>
                  <table>
                    <thead>
                      <tr><th>Type</th><th>Bid</th><th>Ask</th></tr>
                    </thead>
                    <tbody>
                      <tr><td>EUR/USD</td><td>1.0710</td><td>1.0712</td></tr>
                    </tbody>
                  </table>
                </body></html>
                """;

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> invokeParseHtmlToQuote(handler, html, eurUsd)
        );

        assertTrue(ex.getMessage().contains("required columns [Name, Bid, Ask] not found"),
                "Unexpected error message: " + ex.getMessage());
    }

    @Test
    void parseHtml_symbolNotUnderNameColumn_throws() {
        // EUR/USD стоит не под колонкой Name → должна сработать проверка 4.2
        String html = """
                <html><body>
                  <table>
                    <thead>
                      <tr><th>Id</th><th>Bid</th><th>Name</th><th>Ask</th></tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>EUR/USD</td>      <!-- индекс 0 -->
                        <td>1.0710</td>       <!-- индекс 1 (Bid) -->
                        <td>Some name</td>    <!-- индекс 2 (Name) -->
                        <td>1.0712</td>       <!-- индекс 3 (Ask) -->
                      </tr>
                    </tbody>
                  </table>
                </body></html>
                """;

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> invokeParseHtmlToQuote(handler, html, eurUsd)
        );

        assertTrue(ex.getMessage().contains("not in the 'Name' column"),
                "Unexpected error message: " + ex.getMessage());
    }

    @Test
    void parseHtml_rowCellsCountDiffersFromHeader_throws() {
        // В заголовке 3 колонки, в строке с инструментом 4 ячейки → 4.3 должен отработать
        String html = """
                <html><body>
                  <table>
                    <thead>
                      <tr><th>Name</th><th>Bid</th><th>Ask</th></tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>EUR/USD</td>
                        <td>1.0710</td>
                        <td>1.0712</td>
                        <td>EXTRA</td> <!-- лишняя ячейка -->
                      </tr>
                    </tbody>
                  </table>
                </body></html>
                """;

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> invokeParseHtmlToQuote(handler, html, eurUsd)
        );

        assertTrue(ex.getMessage().contains("header has 3 columns but row with instrument symbol has 4"),
                "Unexpected error message: " + ex.getMessage());
    }

    @Test
    void parseHtml_invalidBidFormat_throwsIllegalArgument() {
        // В ячейке Bid недопустимый формат (запятая вместо точки) → toDecimal бросит IllegalArgumentException
        String html = """
                <html><body>
                  <table>
                    <thead>
                      <tr><th>Name</th><th>Bid</th><th>Ask</th></tr>
                    </thead>
                    <tbody>
                      <tr><td>EUR/USD</td><td>1,0710</td><td>1.0712</td></tr>
                    </tbody>
                  </table>
                </body></html>
                """;

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> invokeParseHtmlToQuote(handler, html, eurUsd)
        );

        assertTrue(ex.getMessage().contains("Decimal comma is not allowed"),
                "Unexpected error message: " + ex.getMessage());
    }

    // -----------------------------------------------------------------------------------------------------------------
    // ВСПОМОГАТЕЛЬНЫЙ КОД
    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Хелпер: вызывает приватный метод parseHtmlToQuote(String, FxSpot) через reflection.
     * Если внутри метода бросается RuntimeException (IllegalStateException/IllegalArgumentException),
     * пробрасываем именно её, чтобы assertThrows видел корректный тип.
     */
    private static QuoteTick invokeParseHtmlToQuote(ProFinanceFxSpotHandler handler,
                                                    String html,
                                                    FxSpot instrument) {
        try {
            Method m = ProFinanceFxSpotHandler.class
                    .getDeclaredMethod("parseHtmlToQuote", String.class, FxSpot.class);
            m.setAccessible(true);
            return (QuoteTick) m.invoke(handler, html, instrument);
        } catch (InvocationTargetException e) {
            // Внутри parseHtmlToQuote произошло исключение → достаём исходную причину
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException re) {
                throw re; // пробрасываем как есть (IllegalStateException/IllegalArgumentException)
            }
            throw new RuntimeException("Unexpected checked exception from parseHtmlToQuote", cause);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Reflection invoke failed", e);
        }
    }
}
