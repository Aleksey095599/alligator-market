package com.alligator.market.backend.fx.quote.web;

import com.alligator.core.fx.dto.CurrencyQuoteDto;
import com.alligator.core.fx.model.CurrencyQuote;
import com.alligator.core.fx.port.ExternalPriceFeed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/* Контроллер выдачи котировок через SSE. */
@RestController
@RequestMapping("/api/v1/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final ExternalPriceFeed feed;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CurrencyQuoteDto> stream() {
        return feed.streamAll().map(this::toDto);
    }

    private CurrencyQuoteDto toDto(CurrencyQuote q) {
        return new CurrencyQuoteDto(q.pairId(), q.bid(), q.ask(), q.ts());
    }
}
