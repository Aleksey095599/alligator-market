package com.alligator.market.backend.provider.twelve.free;

import com.alligator.market.domain.instrument.Instrument;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import reactor.test.StepVerifier;

@SpringBootTest
@TestPropertySource("classpath:application.properties")
public class TwelveFreeAdapterTest {

    @Autowired
    TwelveFreeAdapterV2 adapter;

    @Test
    void returnsSingleTick() {

        Instrument eurusd = new Instrument("EUR/USD");

        StepVerifier.create(
                        adapter.streamQuotes(eurusd)
                                .doOnNext(tick -> System.out.println("PRICE = " + tick.bid()))
                )
                .expectNextCount(1) // ровно один тик
                .verifyComplete();
    }
}
