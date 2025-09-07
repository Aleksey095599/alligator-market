package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.base.Instrument;

import java.util.Set;

/**
 * Абстрактный каркас обработчика инструмента.
 */
public abstract class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    // Ссылка на провайдера
    private P provider;

    // Поддерживаемый класс инструмента
    private final Class<I> supportedInstrumentClass;

    // Набор поддерживаемых инструментов
    private final Set<I> supportedInstruments;

    // Конструктор
    protected AbstractInstrumentHandler(Class<I> supportedInstrumentClass,
                                       Set<I> supportedInstruments) {
        this.supportedInstrumentClass = supportedInstrumentClass;
        this.supportedInstruments = Set.copyOf(supportedInstruments);
    }

    // Устанавливаем провайдера
    void setProvider(P provider) {
        this.provider = provider;
    }

    /** Возвращает ссылку на провайдера. */
    @Override
    public P getProvider() {
        return provider;
    }

    /** Возвращает поддерживаемый класс инструмента. */
    @Override
    public Class<I> getSupportedInstrumentClass() {
        return supportedInstrumentClass;
    }

    /** Возвращает набор поддерживаемых инструментов. */
    @Override
    public Set<I> getSupportedInstruments() {
        return supportedInstruments;
    }

    /** Проверяет поддержку указанного инструмента. */
    @Override
    public boolean supportsInstrument(I instrument) {
        return supportedInstruments.contains(instrument);
    }
}
