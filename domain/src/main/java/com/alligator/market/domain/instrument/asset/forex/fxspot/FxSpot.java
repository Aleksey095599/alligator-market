package com.alligator.market.domain.instrument.asset.forex.fxspot;

import com.alligator.market.domain.instrument.asset.forex.fxspot.classification.FxSpotTenor;
import com.alligator.market.domain.instrument.classification.Asset;
import com.alligator.market.domain.instrument.classification.Product;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.fxspot.codec.FxSpotCodec;
import com.alligator.market.domain.instrument.asset.forex.fxspot.exception.FxSpotSameCurrenciesException;

import java.util.Objects;

/**
 * Финансовый инструмент FOREX_SPOT (aggregate root).
 *
 * <p>Примечание: FOREX_SPOT реализует доменный контракт инструмента {@link Instrument}.</p>
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
    public Asset asset() {
        return Asset.FOREX;
    }

    @Override
    public Product product() {
        return Product.SPOT;
    }

}
