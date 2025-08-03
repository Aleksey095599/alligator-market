package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import com.alligator.market.domain.instrument.InstrumentType;
import java.util.Map;
import reactor.core.publisher.Flux;

/**
 * Единый контракт адаптера для всех провайдеров рыночных данных.
 */
public interface MarketDataProvider {

    /** Возвращает профиль провайдера. */
    ProviderProfile profile();

    /** Возвращает карту обработчиков инструментов. */
    Map<InstrumentType, InstrumentHandler> instrumentHandlers();

    /** Возвращает котировку. */
    default Flux<QuoteTick> quote(Instrument instrument) {
        InstrumentHandler handler = instrumentHandlers().get(instrument.instrumentType());
        if (handler != null) {
            return handler.instrumentQuote(instrument);
        }
        return Flux.error(new UnsupportedOperationException(
                "Instrument type " + instrument.instrumentType()
                        + " not supported by " + profile().providerCode()));
    }
}
