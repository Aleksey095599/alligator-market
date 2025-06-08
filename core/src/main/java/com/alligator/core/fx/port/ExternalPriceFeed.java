package com.alligator.core.fx.port;

import com.alligator.core.fx.model.CurrencyQuote;
import reactor.core.publisher.Flux;

/*
 * Источник внешних котировок (HTTP-API, WebSocket, Kafka-topic…).
 * Домен обращается к этому интерфейсу, чтобы получать цены,
 * не зная о конкретном протоколе/провайдере.
 */
public interface ExternalPriceFeed {


    Flux<CurrencyQuote> streamAll();
}
