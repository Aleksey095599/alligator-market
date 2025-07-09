package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.forex.Instrument;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

/**
 * Единый контракт адаптера для всех провайдеров рыночных данных.
 */
public interface MarketDataProvider {

    /* Уникальный код провайдера */
    String providerCode();

    /* Режим доставки рыночных данных провайдера (PULL или PUSH) */
    DeliveryMode deliveryMode();

    /* Метод доступа к рыночным данным провайдера (API_POLL, WEBSOCKET и другие) */
    AccessMethod accessMethod();

    /* Возможность массовой подписки на рыночные данные для нескольких инструментов */
    boolean supportsBulkSubscription();

    /* Подписка на получение рыночных данных для указанного инструмента */
    Flux<QuoteTick> subscribe(Instrument instrument);
}
