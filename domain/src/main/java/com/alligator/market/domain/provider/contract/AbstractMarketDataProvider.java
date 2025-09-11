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

    protected final Profile profile;
    Set<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> handlers();

    // Конструктор
    protected AbstractMarketDataProvider(
            Profile profile;
            Set<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> handlers();
    ) {
        this.profile = profile;
        // Создаем необходимую карту
        Map<Instrument, InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> map = new HashMap<>();
        // Заполняем карту

        this.instrumentHandlerMap = Map.copyOf(map);
    }

    /** Профиль провайдера. */
    @Override
    public Profile profile() {
        return profile;
    }

    /** Набор обработчиков. */
    @Override

    /** Карта инструмент → обработчик. */
    @Override
    Map<Instrument, InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> instrumentHandlerMap() {
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
                    handler.code(),
                    profile().providerCode()
            );
        }
        return handler;
    }
}
