package com.alligator.market.backend.fx.quote.service;

import com.alligator.core.model.CurrencyQuote;
import com.alligator.core.model.QuoteStream;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class QuoteStreamImpl implements QuoteStream {

    /* Многоподписочный неблокирующий sink */
    private final Sinks.Many<CurrencyQuote> sink =
            Sinks.many().multicast().directAllOrNothing();

    @Override
    public Flux<CurrencyQuote> streamAll() {
        return sink.asFlux();
    }

    /* Генерируем фиктивный тик раз в секунду, чтобы фронт уже видел данные. */
    @Scheduled(fixedRate = 1_000)
    void emitStub() {
        var quote = new CurrencyQuote(
                4L,
                new BigDecimal("1.1000"),
                new BigDecimal("1.1005"),
                (short) 2,
                Instant.now()
        );
        sink.tryEmitNext(quote);
    }

}
