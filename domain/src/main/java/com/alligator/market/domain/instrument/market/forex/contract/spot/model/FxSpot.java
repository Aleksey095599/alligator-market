package com.alligator.market.domain.instrument.market.forex.contract.spot.model;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
import com.alligator.market.domain.instrument.model.InstrumentType;
import com.alligator.market.domain.instrument.market.forex.support.currency.model.Currency;
import com.alligator.market.domain.instrument.market.forex.contract.spot.codec.FxSpotCodec;
import com.alligator.market.domain.instrument.market.forex.contract.spot.exception.FxSpotSameCurrenciesException;

import java.util.Objects;

/**
 * Модель финансового инструмента FX_SPOT.
 *
 * <p>Реализует базовый контракт финансового инструмента {@link Instrument}.</p>
 *
 * @param base                       Базовая валюта
 * @param quote                      Котируемая валюта
 * @param tenor                      Тенор даты валютирования
 * @param defaultQuoteFractionDigits Количество знаков после запятой в котировке по умолчанию
 */
public record FxSpot(
        Currency base,
        Currency quote,
        FxSpotTenor tenor,
        int defaultQuoteFractionDigits

) implements Instrument {

    /**
     * Конструктор с проверками.
     */
    public FxSpot {
        Objects.requireNonNull(base, "base must not be null");
        Objects.requireNonNull(quote, "quote must not be null");
        Objects.requireNonNull(tenor, "tenor must not be null");

        // Ограничение на количество знаков после запятой в котировке согласно рыночной практике
        if (defaultQuoteFractionDigits < 0 || defaultQuoteFractionDigits > 10) {
            throw new IllegalArgumentException("defaultQuoteFractionDigits must be between 0 and 10");
        }

        // Валюты не должны совпадать
        if (base.code().equals(quote.code())) {
            throw new FxSpotSameCurrenciesException(base.code(), quote.code());
        }
    }

    @Override
    public InstrumentCode instrumentCode() {
        return FxSpotCodec.fxSpotCode(base.code(), quote.code(), tenor);
    }

    @Override
    public InstrumentSymbol instrumentSymbol() {
        return FxSpotCodec.fxSpotSymbol(base.code(), quote.code(), tenor);
    }

    @Override
    public InstrumentType instrumentType() {
        return InstrumentType.FX_SPOT;
    }
}
