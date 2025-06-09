package com.alligator.market.core.fx.port;

import com.alligator.market.core.fx.model.CurrencyQuote;
import reactor.core.publisher.Flux;

/**
 * Источник внешних котировок валютных пар.
 * Абстрагирует доступ к котировкам от конкретных механизмов получения данных (HTTP API, WebSocket, Kafka и т.д.).
 */
public interface ExternalPriceFeed {

    /** Общий поток всех котировок. */
    Flux<CurrencyQuote> streamAll();

}
