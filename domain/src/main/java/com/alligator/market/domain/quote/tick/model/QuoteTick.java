package com.alligator.market.domain.quote.tick.model;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.MarketDataProvider;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Глобальная модель тика котировки для валютной пары.
 *
 * <p>Используется как унифицированное представление любых рыночных тиков:
 * по последней сделке (LAST) и по котировке bid/ask.</p>
 *
 * @param instrumentCode    доменный код инструмента (соответствует {@link Instrument#instrumentCode()})
 * @param last              цена последней сделки (MOEX LAST); может быть {@code null}
 * @param bid               цена лучшей покупки; может быть {@code null}
 * @param ask               цена лучшей продажи; может быть {@code null},
 * @param exchangeTimestamp время события на стороне биржи/провайдера
 * @param receivedTimestamp время, когда тик был получен и зафиксирован в нашей системе
 * @param providerCode      технический код провайдера, предоставившего котировку (соответствует {@link MarketDataProvider#providerCode()})
 */
public record QuoteTick(
        String instrumentCode,
        BigDecimal last,
        BigDecimal bid,
        BigDecimal ask,
        Instant exchangeTimestamp,
        Instant receivedTimestamp,
        ProviderCode providerCode
) {
    /**
     * Фабрика тика по последней сделке (LAST): Поля "bid"/"ask" {@code null}.
     */
    public static QuoteTick lastTrade(
            String instrumentCode,
            BigDecimal last,
            Instant exchangeTimestamp,
            Instant receivedTimestamp,
            ProviderCode providerCode
    ) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(last, "last must not be null");
        Objects.requireNonNull(exchangeTimestamp, "exchangeTimestamp must not be null");
        Objects.requireNonNull(receivedTimestamp, "receivedTimestamp must not be null");
        Objects.requireNonNull(providerCode, "providerCode must not be null");

        return new QuoteTick(
                instrumentCode,
                last,
                null,
                null,
                exchangeTimestamp,
                receivedTimestamp,
                providerCode
        );
    }

    /**
     * Фабрика тика для котировки "bid"/"ask": поле "last" {@code null}.
     */
    public static QuoteTick bidAsk(
            String instrumentCode,
            BigDecimal bid,
            BigDecimal ask,
            Instant exchangeTimestamp,
            Instant receivedTimestamp,
            ProviderCode providerCode
    ) {
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(bid, "bid must not be null");
        Objects.requireNonNull(ask, "ask must not be null");
        Objects.requireNonNull(exchangeTimestamp, "exchangeTimestamp must not be null");
        Objects.requireNonNull(receivedTimestamp, "receivedTimestamp must not be null");
        Objects.requireNonNull(providerCode, "providerCode must not be null");

        return new QuoteTick(
                instrumentCode,
                null,
                bid,
                ask,
                exchangeTimestamp,
                receivedTimestamp,
                providerCode
        );
    }
}
