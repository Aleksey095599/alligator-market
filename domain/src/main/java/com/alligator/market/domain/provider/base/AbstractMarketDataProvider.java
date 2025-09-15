package com.alligator.market.domain.provider.base;

import com.alligator.market.domain.instrument.contract.Instrument;
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

    /* Карта "инструмент → обработчик".*/
    private final Map<Instrument, InstrumentHandler<P, ? extends Instrument>> instrumentHandlerMap;

    /* F-bounded полиморфизм: даём наследникам вернуть "себя" нужного типа. */
    protected abstract P self();

    /**
     * Конструктор.
     *
     * @throws IllegalStateException если при заполнении карты "инструмент → обработчик" происходит
     *                               повторная регистрация инструмента
     */
    protected AbstractMarketDataProvider(
            ProviderDescriptor providerDescriptor,
            Set<? extends InstrumentHandler<P, ? extends Instrument>> handlers // Набор обработчиков
    ) {
        // ↓↓ Примитивные проверки
        this.providerDescriptor = Objects.requireNonNull(providerDescriptor,
                "providerDescriptor must not be null");
        Objects.requireNonNull(handlers, "handlers must not be null");
        if (handlers.isEmpty()) {
            throw new IllegalArgumentException("handlers must not be empty");
        }

        // 1) Проверяем уникальность handlers по коду
        var codes = new java.util.HashSet<String>();
        for (var h : handlers) {
            var code = h.handlerCode();
            if (!codes.add(code)) {
                throw new IllegalStateException("Provider '" + providerDescriptor.providerCode() +
                        "' contains multiple handlers with the same code '" + code + "'");
            }
        }

        // 2) Собираем карту "инструмент → обработчик"
        var map = new java.util.LinkedHashMap<Instrument, InstrumentHandler<P, ? extends Instrument>>();
        for (var h : handlers) {
            var supported = h.supportedInstruments();
            for (Instrument ins : supported) {
                // Обеспечиваем уникальность ключей (инструментов) в карте при сборке
                var prev = map.putIfAbsent(ins, h);
                if (prev != null) {
                    throw new IllegalStateException("Instrument '" + ins.code() +
                            "' is already bound to handler '" + prev.handlerCode() + "'");
                }
            }
        }

        // 3) Фиксируем карту (делаем неизменяемой)
        this.instrumentHandlerMap = java.util.Collections.unmodifiableMap(map);

        // 4) После того как карта зафиксирована, извлекаем из нее набор неповторяющихся обработчиков.
        //    Может казаться, что мы сначала разложили обработчики из набора по инструментам,
        //    а теперь обратно собираем в набор. Это хорошая практика, безопасно.
        for (var h : new java.util.LinkedHashSet<>(map.values())) {
            h.attachTo(self()); // ← attachTo должен только сохранить ссылку и ничего не вызывать
        }
    }

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
        InstrumentHandler<P, I> handler = (InstrumentHandler<P, I>) instrumentHandlerMap.get(instrument);
        if (handler == null) {
            throw new HandlerNotFoundException(
                    instrument.code(),
                    providerDescriptor.providerCode()
            );
        }
        return handler.quote(instrument);
    }
}
