package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Абстрактный каркас обработчика инструмента.
 */
public abstract class AbstractInstrumentHandler<P extends MarketDataProvider, I extends Instrument>
        implements InstrumentHandler<P, I> {

    // Ссылка на провайдера
    private P provider;

    // Поддерживаемый тип инструмента
    private final InstrumentType supportedInstrumentType;

    // Набор поддерживаемых инструментов
    private final Set<I> supportedInstruments;

    // Конструктор
    protected AbstractInstrumentHandler(InstrumentType supportedInstrumentType,
                                       Set<I> supportedInstruments) {
        this.supportedInstrumentType = supportedInstrumentType;
        // Сохраняем инструменты и проверяем их тип
        this.supportedInstruments = supportedInstruments.stream()
                .peek(i -> {
                    if (i.getType() != supportedInstrumentType) {
                        throw new IllegalArgumentException(
                                "Instrument type mismatch: %s".formatted(i.getType())
                        );
                    }
                })
                .collect(Collectors.toUnmodifiableSet());
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

    /** Возвращает поддерживаемый тип инструмента. */
    @Override
    public InstrumentType getSupportedInstrumentType() {
        return supportedInstrumentType;
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
