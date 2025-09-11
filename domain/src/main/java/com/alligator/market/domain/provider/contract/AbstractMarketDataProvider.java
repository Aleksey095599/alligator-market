package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.exception.InstrumentNotSupportedException;
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

    /** Профиль провайдера. */
    protected final Profile profile;

    /** Набор обработчиков. */
    protected final Set<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> handlers;

    /** Карта инструмент → обработчик. */
    protected final Map<Instrument, InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> instrumentHandlerMap;

    /**
     * Конструктор.
     *
     * @param profile  профиль провайдера
     * @param handlers набор обработчиков
     */
    protected AbstractMarketDataProvider(
            Profile profile,
            Set<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> handlers
    ) {
        this.profile = Objects.requireNonNull(profile, "profile must not be null");
        Objects.requireNonNull(handlers, "handlers must not be null");

        // Делаем набор обработчиков неизменяемым
        this.handlers = Set.copyOf(handlers);

        // Собираем карту инструмент → обработчик
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
     * @throws InstrumentNotSupportedException если обработчик не найден
     */
    @Override
    public InstrumentHandler<? extends MarketDataProvider, ? extends Instrument> findHandler(Instrument instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");
        InstrumentHandler<? extends MarketDataProvider, ? extends Instrument> handler = instrumentHandlerMap.get(instrument);
        if (handler == null) {
            throw new InstrumentNotSupportedException(
                    instrument.code(),
                    "unknown",
                    profile().providerCode()
            );
        }
        return handler;
    }
}

