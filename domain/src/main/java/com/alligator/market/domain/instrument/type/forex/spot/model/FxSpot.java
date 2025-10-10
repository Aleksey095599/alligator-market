package com.alligator.market.domain.instrument.type.forex.spot.model;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.spot.codec.FxSpotCodec;
import com.alligator.market.domain.instrument.type.forex.spot.exception.FxSpotSameCurrenciesException;

import java.util.Objects;

/**
 * Модель финансового инструмента FX_SPOT.
 *
 * @param base                         Базовая валюта
 * @param quote                        Котируемая валюта
 * @param valueDate                    Дата валютирования
 * @param defaultQuoteFractionDigits   Количество знаков после запятой в котировке по умолчанию
 *
 * @see Instrument                     Часть параметров переопределены из базового контракта
 */
public record FxSpot(
        Currency base,
        Currency quote,
        FxSpotValueDate valueDate,
        int defaultQuoteFractionDigits
) implements Instrument {

    /** Конструктор с проверками. */
    public FxSpot {
        // ↓↓ Базовые проверки аргументов
        Objects.requireNonNull(base, "base must not be null");
        Objects.requireNonNull(quote, "quote must not be null");
        Objects.requireNonNull(valueDate, "valueDateCode must not be null");

        // Ограничение на количество знаков после запятой в котировке согласно рыночной практике
        if (defaultQuoteFractionDigits < 0 || defaultQuoteFractionDigits > 10) {
            throw new IllegalArgumentException("defaultQuoteFractionDigits must be between 0 and 10");
        }

        // Валюты не должны совпадать
        if (base.code().equals(quote.code())) {
            throw new FxSpotSameCurrenciesException(base.code(), quote.code());
        }
    }

    /** Внутренний код инструмента (уникален в контексте приложения). */
    @Override
    public String code() {
        return FxSpotCodec.fxSpotCode(base, quote, valueDate);
    }

    /** Символ инструмента для отображения в UI. */
    @Override
    public String symbol() {
        return FxSpotCodec.fxSpotSymbol(base, quote, valueDate);
    }

    /** Тип инструмента. */
    @Override
    public InstrumentType type() {
        return InstrumentType.FX_SPOT;
    }
}
