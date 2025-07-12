package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

/**
 * Единый контракт адаптера для всех провайдеров рыночных данных.
 */
public interface MarketDataProvider {

    //-----------------------
    // Статические метаданные
    //-----------------------

    /* Уникальный код провайдера */
    String providerCode();

    /* Режим доставки рыночных данных провайдера (PULL или PUSH) */
    DeliveryMode deliveryMode();

    /* Метод доступа к рыночным данным провайдера (API_POLL, WEBSOCKET и другие) */
    AccessMethod accessMethod();

    /* Возможность массовой подписки на рыночные данные для нескольких инструментов */
    boolean supportsBulkSubscription();

    //===========================
    // Динамические потоки данных
    //===========================

    /**
     * Поток котировок для заданного инструмента.
     */
    Flux<QuoteTick> streamQuotes(Instrument instrument);
}
