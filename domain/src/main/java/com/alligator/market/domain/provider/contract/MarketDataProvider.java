package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.provider.exeption.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import com.alligator.market.domain.instrument.model.InstrumentType;

import java.util.Map;
import java.util.Set;
import reactor.core.publisher.Flux;

/**
 * Контракт провайдера рыночных данных.
 */
public interface MarketDataProvider {

    /** Возвращает профиль провайдера. */
    ProviderProfile profile();

    /** Возвращает карту: тип инструмента → обработчик. */
    Map<InstrumentType, InstrumentHandler> instrumentHandlers();

    /**
     * Возвращает котировку.
     *
     * @throws InstrumentNotSupportedException если подходящий обработчик инструмента не найден
     */
    default Flux<QuoteTick> quote(Instrument instrument) {
        // Извлекаем тип инструмента
        InstrumentType instrumentType = instrument.type();
        // Подбираем нужный обработчик для данного типа инструмента
        InstrumentHandler handler = instrumentHandlers().get(instrumentType);
        // Проверка, что обработчик существует
        if (handler == null) {
            return Flux.error(new InstrumentNotSupportedException(instrumentType, profile().providerCode()));
        }
        // Возвращаем котировку инструмента
        return handler.instrumentQuote(instrument);
    }
}
