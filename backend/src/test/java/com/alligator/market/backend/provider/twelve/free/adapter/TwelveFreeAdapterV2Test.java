package com.alligator.market.backend.provider.twelve.free.adapter;

import com.alligator.market.backend.provider.adapter.twelve.free.TwelveFreeAdapterV2;
import com.alligator.market.backend.provider.adapter.twelve.free.config.TwelveFreeConnectionProps;
import com.alligator.market.domain.instrument.type.fx.outright.reference.currency_pair.model.CurrencyPair;
import com.alligator.market.domain.instrument.type.fx.outright.model.FxOutright;
import com.alligator.market.domain.instrument.type.fx.outright.model.ValueDateCode;
import com.alligator.market.domain.quote.QuoteTick;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Интеграционный тест для адаптера TwelveFreeAdapterV2.
 * Выводит в консоль котировку валютной пары EUR/USD.
 */
@Disabled
class TwelveFreeAdapterV2Test {

    @Test
    void shouldFetchRealQuote() {
        // Параметры подключения к провайдеру
        TwelveFreeConnectionProps props = new TwelveFreeConnectionProps(
                "https://api.twelvedata.com",
                "2b8e2659372340d5b922cd6b8d6d2cb2"
        );
        WebClient client = WebClient.builder()
                .baseUrl(props.baseUrl())
                .build();

        TwelveFreeAdapterV2 adapter = new TwelveFreeAdapterV2(props, client);

        CurrencyPair pair = new CurrencyPair("EUR", "USD", 2);
        FxOutright fxOutright = new FxOutright(pair, ValueDateCode.TOM);

        QuoteTick tick = adapter.quote(fxOutright)
                .blockFirst(Duration.ofSeconds(5));

        assertNotNull(tick);
        System.out.println(tick);
    }
}
