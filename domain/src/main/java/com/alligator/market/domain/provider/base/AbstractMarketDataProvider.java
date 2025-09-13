package com.alligator.market.domain.provider.base;

import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.exception.HandlerNotFoundException;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.*;

/**
 * Абстрактный каркас провайдера рыночных данных.
 */
public abstract class AbstractMarketDataProvider<P extends MarketDataProvider> implements MarketDataProvider {

    /* Дескриптор провайдера. */
    protected final ProviderDescriptor providerDescriptor;

    /* Карта "инструмент → обработчик". После инициализации делаем неизменяемой. */
    private final Map<Instrument, InstrumentHandler<P, ? extends Instrument>> instrumentHandlers;

    /* Конструктор. */
    protected AbstractMarketDataProvider(
            ProviderDescriptor providerDescriptor,
            Set<? extends InstrumentHandler<P, ? extends Instrument>> handlers // Передаем набор обработчиков
    ) {
        this.providerDescriptor = Objects.requireNonNull(providerDescriptor,
                "providerDescriptor must not be null");
        Objects.requireNonNull(handlers, "handlers must not be null");

        // ↓↓ Собираем карту "инструмент → обработчик"
        Map<Instrument, InstrumentHandler<P, ? extends Instrument>> map = new HashMap<>();
        for (InstrumentHandler<P, ? extends Instrument> h : handlers) { // Перебираем обработчики
            h.attachTo(self());
            for (Instrument ins : h.supportedInstruments()) { // Перебираем поддерживаемые обработчиком инструменты
                // Защита от повторной регистрации инструмента
                InstrumentHandler<P, ? extends Instrument> prev = map.putIfAbsent(ins, h);
                if (prev != null && prev != h) {
                    throw new IllegalStateException(
                            "Instrument '" + ins.code() + "' is already bound to another handler (" +
                                    prev.handlerCode() + ") for provider '" + providerDescriptor.providerCode() + "'"
                    );
                }
            }
        }
        this.instrumentHandlers = Collections.unmodifiableMap(map);
    }

    /* F-bounded полиморфизм: даём наследникам вернуть "себя" нужного типа. */
    protected abstract P self();

    /** Дескриптор провайдера: неизменяемый набор статических атрибутов. */
    @Override
    public ProviderDescriptor descriptor() {
        return providerDescriptor;
    }

    /** Унифицированная операция получения котировок для любого поддерживаемого инструмента. */
    @Override
    public final <I extends Instrument> Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");
        @SuppressWarnings("unchecked")
        InstrumentHandler<P, I> handler = (InstrumentHandler<P, I>) instrumentHandlers.get(instrument);
        if (handler == null) {
            throw new HandlerNotFoundException(
                    instrument.code(),
                    providerDescriptor.providerCode()
            );
        }
        return handler.quote(instrument);
    }

    /* Защищённый доступ, если нужно внутри наследников. */
    protected final <I extends Instrument> InstrumentHandler<P, I> findHandler(I instrument) {
        @SuppressWarnings("unchecked")
        InstrumentHandler<P, I> h = (InstrumentHandler<P, I>) instrumentHandlers.get(instrument);
        return h;
    }

    /* Для метрик/диагностики. */
    protected final Map<Instrument, InstrumentHandler<P, ? extends Instrument>> instrumentHandlerMap() {
        return instrumentHandlers;
    }
}
