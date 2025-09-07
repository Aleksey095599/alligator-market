package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.type.InstrumentType;

/**
 * Абстрактный каркас обработчика инструмента.
 */
public abstract class AbstractInstrumentHandler<P extends MarketDataProvider>
        implements InstrumentHandler<P> {

    // Ссылка на провайдера
    private P provider;

    // Код провайдера
    private final String providerCode;

    // Поддерживаемый тип инструмента
    private final InstrumentType supportedInstrumentType;

    // Конструктор
    protected AbstractInstrumentHandler(String providerCode, InstrumentType supportedInstrumentType) {
        this.providerCode = providerCode;
        this.supportedInstrumentType = supportedInstrumentType;
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

    /** Возвращает код провайдера рыночных данных, к которому относится обработчик. */
    @Override
    public String getProviderCode() {
        return providerCode;
    }

    /** Возвращает поддерживаемый тип инструмента. */
    @Override
    public InstrumentType getSupportedInstrumentType() {
        return supportedInstrumentType;
    }
}
