package com.alligator.market.domain.instrument.asset.forex.type.spot.exception;

import com.alligator.market.domain.common.exception.BaseDomainException;
import com.alligator.market.domain.common.exception.DomainErrorCode;
import com.alligator.market.domain.instrument.asset.forex.support.currency.vo.CurrencyCode;

import java.util.Objects;

/**
 * Ошибка: базовая и котируемая валюты совпадают.
 */
public final class FxSpotSameCurrenciesException extends BaseDomainException {

    private final CurrencyCode base;
    private final CurrencyCode quote;

    /**
     * Создает исключение.
     *
     * @param base  код базовой валюты
     * @param quote код котируемой валюты
     */
    public FxSpotSameCurrenciesException(CurrencyCode base, CurrencyCode quote) {
        super(DomainErrorCode.FX_SPOT_SAME_CURRENCIES, msg(base, quote));
        this.base = Objects.requireNonNull(base, "base must not be null");
        this.quote = Objects.requireNonNull(quote, "quote must not be null");
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
        super(DomainErrorCode.FX_SPOT_SAME_CURRENCIES, msg(base, quote), cause);
        this.base = Objects.requireNonNull(base, "base must not be null");
        this.quote = Objects.requireNonNull(quote, "quote must not be null");
    }

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
     * Возвращает код базовой валюты.
     *
     * @return код базовой валюты
     */
    @SuppressWarnings("unused")
    public CurrencyCode getBaseCurrency() {
        return base;
    }

    /**
     * Возвращает код котируемой валюты.
     *
     * @return код котируемой валюты
     */
    @SuppressWarnings("unused")
    public CurrencyCode getQuoteCurrency() {
        return quote;
    }
}
