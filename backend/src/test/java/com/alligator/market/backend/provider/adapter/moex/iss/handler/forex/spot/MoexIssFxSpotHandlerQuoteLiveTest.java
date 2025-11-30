package com.alligator.market.backend.provider.adapter.moex.iss.handler.forex.spot;

import com.alligator.market.backend.provider.adapter.moex.iss.MoexIssAdapter;
import com.alligator.market.backend.provider.adapter.moex.iss.config.MoexIssAdapterProps;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpotValueDate;
import com.alligator.market.domain.quote.QuoteTick;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * "Живой" интеграционный тест quote(...) для {@link MoexIssFxSpotHandler} против реального MOEX ISS.
 */
class MoexIssFxSpotHandlerQuoteLiveTest {

    @Test
    @Tag("external")
    @Tag("slow")
    void liveQuoteForCnyRubTom() {
        // 1) Реальный baseUrl MOEX ISS
        String baseUrl = "https://iss.moex.com/iss";
        MoexIssAdapterProps props = new MoexIssAdapterProps(baseUrl);

        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("User-Agent", "Alligator Market TEST")
                .build();

        // 2) Реальный адаптер + хендлер
        MoexIssFxSpotHandler handler = new MoexIssFxSpotHandler(props, webClient);
        handler.attachTo(new MoexIssAdapter(props, webClient));

        // 3) Доменный инструмент CNYRUB_TOM
        Currency cny = new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);
        Currency rub = new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russian Federation", 2);
        FxSpot cnyRubTom = new FxSpot(cny, rub, FxSpotValueDate.TOM, 4);

        // 4) Запускаем запрос к реальному MOEX ISS
        Mono<QuoteTick> result = Mono.from(handler.quote(cnyRubTom));

        // Сейчас doQuote возвращает пустой Mono, так что мы просто проверяем успешное завершение,
        // а сырой JSON смотрим в System.out / логах.
        StepVerifier
                .create(result)
                .verifyComplete();
    }
}
