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

    /* Карта "инструмент → обработчик". */
    private final Map<Instrument, InstrumentHandler<P, ? extends Instrument>> instrumentHandlerMap;

    /* F-bounded полиморфизм: даём наследникам вернуть "себя" нужного типа. */
    protected abstract P self();

    /**
     * Конструктор с проверками.
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

        // 1) Проверяем уникальность handlerCode
        var seenCodes = new java.util.HashSet<String>();
        for (var h : handlers) {
            var code = Objects.requireNonNull(h.handlerCode(), "handlerCode must not be null").trim();
            if (code.isEmpty()) throw new IllegalArgumentException("handlerCode must not be blank");
            if (!seenCodes.add(code)) {
                throw new IllegalStateException("Duplicate handlerCode '" + code + "' in provider '" +
                        providerDescriptor.providerCode() + "'");
            }
        }

        // 2) Собираем карту "инструмент → обработчик" и ловим возможные пересечения по инструментам
        var map = new java.util.LinkedHashMap<Instrument, InstrumentHandler<P, ? extends Instrument>>();
        for (var h : handlers) {
            var clazz = Objects.requireNonNull(h.instrumentClass(), "instrumentClass must not be null");
            var supported = Objects.requireNonNull(h.supportedInstruments(),
                    "supportedInstruments must not be null");

            for (Instrument ins : supported) {
                Objects.requireNonNull(ins, "supported instrument must not be null");

                // ✓ Ранняя диагностика: инструмент должен соответствовать заявленному классу обработчика
                if (!clazz.isInstance(ins)) {
                    throw new IllegalStateException("Handler '" + h.handlerCode() + "' lists instrument '" +
                            ins.code() + "' of type '" + ins.getClass().getName() +
                            "' which is not instance of declared " + clazz.getName());
                }

                // ✓ Уникальность по равенству Instrument (equals/hashCode стандартизированы у вас)
                var prev = map.putIfAbsent(ins, h);
                if (prev != null && prev != h) {
                    throw new IllegalStateException("Instrument '" + ins.code() +
                            "' is already bound to handler '" + prev.handlerCode() +
                            "' in provider '" + providerDescriptor.providerCode() + "'");
                }
            }
        }

        // 3) Фиксируем инвариант: делаем карту неизменяемой
        this.instrumentHandlerMap = java.util.Collections.unmodifiableMap(map);

        // 4) После фиксации инвариантов — прикрепляем обработчики к провайдеру
        //    (dedup, если один обработчик покрывает несколько инструментов)
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

    /**
     * Возвращает обработчик для указанного инструмента или {@code null}, если обработчик не зарегистрирован.
     * Метод предназначен для использования внутри наследников, когда требуется доступ к обработчику
     * без генерации {@link HandlerNotFoundException}.
     */
    protected final <I extends Instrument> InstrumentHandler<P, I> findHandler(I instrument) {
        @SuppressWarnings("unchecked")
        InstrumentHandler<P, I> h = (InstrumentHandler<P, I>) instrumentHandlerMap.get(instrument);
        return h;
    }

    /**
     * Предоставляет неизменяемую карту соответствий «инструмент → обработчик».
     * Используется наследниками для метрик, мониторинга или диагностики.
     */
    protected final Map<Instrument, InstrumentHandler<P, ? extends Instrument>> instrumentHandlerMap() {
        return instrumentHandlerMap;
    }
}
