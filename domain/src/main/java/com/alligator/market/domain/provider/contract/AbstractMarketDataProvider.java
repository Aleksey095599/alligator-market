package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
import com.alligator.market.domain.provider.handler.contract.AbstractInstrumentHandler;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Абстрактный каркас провайдера рыночных данных.
 */
public abstract class AbstractMarketDataProvider implements MarketDataProvider {

    // Профиль провайдера
    protected final Profile profile;

    // Карта обработчиков по инструментам
    protected final Map<Instrument, InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> handlersMap;

    // Конструктор
    protected AbstractMarketDataProvider(
            Collection<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> handlers,
            Profile profile
    ) {
        this.profile = profile;
        Map<Instrument, InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> map = new HashMap<>();
        handlers.forEach(h -> {
            if (h instanceof AbstractInstrumentHandler<?, ?> handler) {
                // Передаем ссылку на провайдера
                ((AbstractInstrumentHandler) handler).setProvider(this);
            }
            h.getSupportedInstruments().forEach(i -> map.put(i, h));
        });
        this.handlersMap = Map.copyOf(map);
    }

    /** Возвращает профиль провайдера. */
    @Override
    public Profile getProfile() {
        return profile;
    }

    /** Возвращает карту обработчиков. */
    @Override
    public Map<Instrument, InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> getHandlers() {
        return handlersMap;
    }

    /**
     * Извлекает обработчик для указанного инструмента.
     *
     * @throws InstrumentNotSupportedException если обработчик не найден
     */
    @Override
    public InstrumentHandler<? extends MarketDataProvider, ? extends Instrument> findHandler(Instrument instrument)
            throws InstrumentNotSupportedException {
        InstrumentHandler<? extends MarketDataProvider, ? extends Instrument> handler = handlersMap.get(instrument);
        if (handler == null) {
            throw new InstrumentNotSupportedException(instrument.type(), getProfile().providerCode());
        }
        return handler;
    }

    /**
     * Возвращает котировку.
     *
     * @throws InstrumentNotSupportedException если подходящий обработчик инструмента не найден
     */
    @Override
    public Publisher<QuoteTick> getQuote(Instrument instrument) throws InstrumentNotSupportedException {
        @SuppressWarnings("unchecked")
        InstrumentHandler<? extends MarketDataProvider, Instrument> handler =
                (InstrumentHandler<? extends MarketDataProvider, Instrument>) findHandler(instrument);
        return handler.getQuote(instrument);
    }
}
