package com.alligator.market.domain.provider;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.quote.QuoteTick;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Set;

/**
 * Единый контракт адаптера для всех провайдеров рыночных данных.
 */
public interface MarketDataProvider {

    //==================================
    // Статические метаданные провайдера
    //==================================

    /* Технический идентификатор провайдера (ключ в БД, конфиг-файлах) */
    String providerCode();

    /* Читаемое имя для UI/логов («Twelve Data — Free plan») */
    String displayName();

    /* Поддерживаемые классы инструментов (FOREX, CRYPTO, …) */
    Set<InstrumentType> instrumentTypes();

    /* Режим доставки рыночных данных: PULL (request/response) или PUSH (stream) */
    DeliveryMode deliveryMode();

    /* Конкретный транспортный метод: API_POLL, WEBSOCKET, FIX, … */
    AccessMethod accessMethod();

    /* Возможна ли массовая подписка одним запросом (symbols=EUR,GBP,JPY) */
    boolean supportsBulkSubscription();

    /* Минимально допустимый интервал опроса.
       Для PUSH-провайдеров вернуть Duration.ZERO. */
    Duration minPollPeriod();

    //===========================
    // Поток котировок провайдера
    //===========================

    /** Поток котировок для заданного инструмента. */
    Flux<QuoteTick> streamQuotes(Instrument instrument);
}
