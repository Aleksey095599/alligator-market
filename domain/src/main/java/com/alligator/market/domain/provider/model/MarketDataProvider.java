package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import com.alligator.market.domain.instrument.model.InstrumentType;

import java.util.Map;
import java.util.Set;
import reactor.core.publisher.Flux;

/**
 * Контракт адаптера для всех провайдеров рыночных данных (далее - провайдеры).
 */
public interface MarketDataProvider {

    /** Возвращает профиль провайдера. */
    ProviderProfile profile();

    /** Возвращает карту: тип инструмента → обработчик. */
    Map<InstrumentType, InstrumentHandler> instrumentHandlers();

    /** Возвращает множество поддерживаемых типов инструментов. */
    default Set<InstrumentType> supportedInstrumentTypes() {
        return instrumentHandlers().keySet();
    }

    /**
     * Возвращает котировку.
     *
     * @throws InstrumentNotSupportedException если подходящий обработчик инструмента не найден
     */
    default Flux<QuoteTick> quote(Instrument instrument) {

        // Извлекаем тип инструмента
        InstrumentType instrumentType = instrument.instrumentType();

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
