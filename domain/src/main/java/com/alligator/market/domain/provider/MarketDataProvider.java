package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

import java.util.Set;

/**
 * Единый контракт адаптера для всех провайдеров рыночных данных.
 */
public interface MarketDataProvider {

    //==================================
    // Статические метаданные провайдера
    //==================================

    /* Уникальный код-идентификатор провайдера */
    String providerCode();

    /* Режим доставки рыночных данных (PULL или PUSH) */
    DeliveryMode deliveryMode();

    /* Метод доступа к рыночным данным (API_POLL, WEBSOCKET и другие) */
    AccessMethod accessMethod();

    /* Возможность массовой подписки на рыночные данные для нескольких инструментов */
    boolean supportsBulkSubscription();

    /* Поддерживаемые инструменты */
    Set<InstrumentType> instrumentTypes();

    //===========================
    // Поток котировок провайдера
    //===========================

    /** Поток котировок для заданного инструмента. */
    Flux<QuoteTick> streamQuotes(Instrument instrument);
}
