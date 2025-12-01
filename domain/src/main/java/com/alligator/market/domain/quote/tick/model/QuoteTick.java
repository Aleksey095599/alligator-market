package com.alligator.market.domain.quote.tick.model;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * <b>Глобальная модель тика котировки для валютной пары.</b>
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
        String providerCode
) {
    /**
     * <b>Фабрика для тика по последней сделке (LAST).</b>
     *
     * <p>Поля bid/ask остаются пустые ({@code null}).</p>
     */
    public static QuoteTick lastTrade(
            String instrumentCode,
            BigDecimal last,
            Instant exchangeTimestamp,
            Instant receivedTimestamp,
            String providerCode
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
     * <b>Фабрика для тика для котировки bid/ask.</b>
     *
     * <p><p>Поле last остаётся пустым ({@code null}).</p>
     */
    public static QuoteTick level1Quote(
            String instrumentCode,
            BigDecimal bid,
            BigDecimal ask,
            Instant exchangeTimestamp,
            Instant receivedTimestamp,
            String providerCode
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
