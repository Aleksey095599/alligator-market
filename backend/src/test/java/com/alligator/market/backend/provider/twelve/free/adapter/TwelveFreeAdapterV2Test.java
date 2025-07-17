package com.alligator.market.backend.provider.twelve.free.adapter;

import com.alligator.market.backend.provider.twelve.free.config.TwelveFreeProps;
import com.alligator.market.domain.instrument.forex.currency_pair.CurrencyPair;
import com.alligator.market.domain.quote.QuoteTick;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/* Интеграционный тест для адаптера TwelveFreeAdapterV2. */
@Disabled
class TwelveFreeAdapterV2Test {

    @Test
    void shouldFetchRealQuote() {
        // Параметры подключения к провайдеру
        TwelveFreeProps props = new TwelveFreeProps(
                "https://api.twelvedata.com",
                "2b8e2659372340d5b922cd6b8d6d2cb2"
        );
        WebClient client = WebClient.builder()
                .baseUrl(props.baseUrl())
                .build();

        TwelveFreeAdapterV2 adapter = new TwelveFreeAdapterV2(props, client);

        CurrencyPair pair = new CurrencyPair("EUR", "USD", "EURUSD", 2);

        QuoteTick tick = adapter.streamQuotes(pair)
                .blockFirst(Duration.ofSeconds(5));

        assertNotNull(tick);
        System.out.println(tick);
    }
}
