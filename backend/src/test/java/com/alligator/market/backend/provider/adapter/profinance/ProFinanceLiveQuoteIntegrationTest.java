package com.alligator.market.backend.provider.adapter.profinance;

import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotValueDate;
import com.alligator.market.domain.quote.QuoteTick;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Интеграционный тест запрашивает живую котировку EUR/USD у провайдера ProFinance.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration")
class ProFinanceLiveQuoteIntegrationTest {

    /* Реальный адаптер провайдера, собираемый Spring. */
    @Autowired
    private ProFinanceAdapter proFinanceAdapter;

    @Test
    void getSingleLiveQuote_eurUsd() {
        // 1) Готовим реальный инструмент EUR/USD как в юнит‑тесте
        Currency eur = new Currency(CurrencyCode.of("EUR"), "Euro", "European Union", 2);
        Currency usd = new Currency(CurrencyCode.of("USD"), "United States Dollar", "United States", 2);
        FxSpot eurUsd = new FxSpot(eur, usd, FxSpotValueDate.TOM, 4);
        assertEquals(InstrumentType.FX_SPOT, eurUsd.instrumentType());

        // 2) Запрашиваем поток котировок у провайдера
        Flux<QuoteTick> flux = Flux.from(proFinanceAdapter.quote(eurUsd));

        // 3) Берём только первую котировку и ждём её с разумным таймаутом
        QuoteTick tick = flux
                .next()                          // первый элемент из потока
                .block(Duration.ofSeconds(10));  // ждём не дольше 10 секунд

        // 4) Простейшие проверки
        assertNotNull(tick, "QuoteTick must not be null");
        assertEquals(eurUsd.instrumentCode(), tick.instrumentCode());
        assertEquals("PROFINANCE", tick.providerCode());
        assertNotNull(tick.bid());
        assertNotNull(tick.ask());
        assertTrue(tick.bid().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(tick.ask().compareTo(BigDecimal.ZERO) > 0);
    }
}
