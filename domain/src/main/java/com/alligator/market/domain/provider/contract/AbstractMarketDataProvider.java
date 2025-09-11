package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.exception.HandlerNotFoundException;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.profile.model.Profile;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Абстрактный каркас провайдера рыночных данных.
 */
public abstract class AbstractMarketDataProvider implements MarketDataProvider {

    protected final Profile profile;
    private final Set<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> handlers;
    private final Map<Instrument, InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> instrumentHandlerMap;

    // Конструктор
    protected AbstractMarketDataProvider(
            Profile profile,
            Set<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> handlers
    ) {
        this.profile = Objects.requireNonNull(profile, "profile must not be null");
        this.handlers = Set.copyOf(Objects.requireNonNull(handlers, "handlers must not be null"));
        // Создаем карту инструментов
        Map<Instrument, InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> map = new HashMap<>();
        for (InstrumentHandler<? extends MarketDataProvider, ? extends Instrument> handler : this.handlers) {
            for (Instrument instrument : handler.supportedInstruments()) {
                map.put(instrument, handler);
            }
        }
        this.instrumentHandlerMap = Map.copyOf(map);
    }

    /** Профиль провайдера. */
    @Override
    public Profile profile() {
        return profile;
    }

    /** Набор обработчиков. */
    @Override
    public Set<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> handlers() {
        return handlers;
    }

    /** Карта инструмент → обработчик. */
    @Override
    public Map<Instrument, InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> instrumentHandlerMap() {
        return instrumentHandlerMap;
    }

    /**
     * Возвращает обработчик для указанного инструмента.
     *
     * @throws HandlerNotFoundException если обработчик не найден
     */
    @Override
    public InstrumentHandler<? extends MarketDataProvider, ? extends Instrument> findHandler(Instrument instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");
        InstrumentHandler<? extends MarketDataProvider, ? extends Instrument> handler = instrumentHandlerMap.get(instrument);
        if (handler == null) {
            throw new HandlerNotFoundException(
                    instrument.code(),
                    profile.providerCode()
            );
        }
        return handler;
    }
}
