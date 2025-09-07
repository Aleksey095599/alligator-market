package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import java.util.Map;

/**
 * Абстрактный каркас провайдера рыночных данных.
 */
public abstract class AbstractMarketDataProvider implements MarketDataProvider {

    // Профиль провайдера
    protected final Profile profile;

    // Карта обработчиков инструментов
    protected final Map<InstrumentType, InstrumentHandler> handlersMap;

    // Конструктор
    protected AbstractMarketDataProvider(Map<InstrumentType, InstrumentHandler> handlersMap, Profile profile) {
        this.profile = profile;
        this.handlersMap = Map.copyOf(handlersMap);
    }

    /** Возвращает профиль провайдера. */
    @Override
    public Profile getProfile() {
        return profile;
    }

    /** Возвращает карту обработчиков. */
    @Override
    public Map<InstrumentType, InstrumentHandler> getHandlers() {
        return handlersMap;
    }

    /**
     * Возвращает котировку.
     *
     * @throws InstrumentNotSupportedException если подходящий обработчик инструмента не найден
     */
    @Override
    public Publisher<QuoteTick> getQuote(Instrument instrument) throws InstrumentNotSupportedException {
        InstrumentType type = instrument.getType();
        InstrumentHandler handler = handlersMap.get(type);
        if (handler == null) {
            return Flux.error(new InstrumentNotSupportedException(type, getProfile().providerCode()));
        }
        return handler.getInstrumentQuote(instrument);
    }
}
