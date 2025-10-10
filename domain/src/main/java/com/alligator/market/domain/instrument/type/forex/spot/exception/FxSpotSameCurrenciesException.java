package com.alligator.market.domain.instrument.type.forex.spot.exception;

import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка: базовая и котируемая валюты совпадают.
 */
public final class FxSpotSameCurrenciesException extends RuntimeException {

    private final CurrencyCode base;
    private final CurrencyCode quote;

    /**
     * Формирует сообщение об ошибке.
     *
     * @param base  код базовой валюты
     * @param quote код котируемой валюты
     * @return текст сообщения
     */
    private static String msg(CurrencyCode base, CurrencyCode quote) {
        CurrencyCode b = Objects.requireNonNull(base, "base must not be null");
        CurrencyCode q = Objects.requireNonNull(quote, "quote must not be null");
        return "Base and quote currencies must be different (base=" + b.value() + ", quote=" + q.value() + ")";
    }

    /**
     * Создает исключение.
     *
     * @param base  код базовой валюты
     * @param quote код котируемой валюты
     */
    @SuppressWarnings("unused")
    public FxSpotSameCurrenciesException(CurrencyCode base, CurrencyCode quote) {
        super(msg(base, quote));
        this.base = base;
        this.quote = quote;
    }

    /**
     * Создает исключение с причиной.
     *
     * @param base  код базовой валюты
     * @param quote код котируемой валюты
     * @param cause причина ошибки
     */
    @SuppressWarnings("unused")
    public FxSpotSameCurrenciesException(CurrencyCode base, CurrencyCode quote, Throwable cause) {
        super(msg(base, quote), cause);
        this.base = base;
        this.quote = quote;
    }

    /**
     * Возвращает код базовой валюты.
     *
     * @return код базовой валюты
     */
    public CurrencyCode getBase() { return base; }

    /**
     * Возвращает код котируемой валюты.
     *
     * @return код котируемой валюты
     */
    public CurrencyCode getQuote() { return quote; }
}

