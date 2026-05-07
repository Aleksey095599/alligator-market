package com.alligator.market.domain.source;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.tick.level.source.SourceMarketDataTick;
import com.alligator.market.domain.source.exception.HandlerNotFoundException;
import com.alligator.market.domain.source.handler.InstrumentHandler;
import com.alligator.market.domain.source.passport.ProviderPassport;
import com.alligator.market.domain.source.policy.ProviderPolicy;
import com.alligator.market.domain.source.vo.HandlerCode;
import com.alligator.market.domain.source.vo.ProviderCode;
import org.reactivestreams.Publisher;

import java.util.*;

/**
 * Base implementation of a runtime market data source.
 */
public abstract class AbstractMarketDataSource<P extends MarketDataSource>
        implements MarketDataSource {

    protected final ProviderCode providerCode;
    protected final ProviderPassport passport;
    protected final ProviderPolicy policy;

    /* Карта "код инструмента → обработчик инструмента". */
    private final Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>> instrumentHandlerMap;

    /**
     * F-bounded полиморфизм: возвращает текущий экземпляр source в его конкретном дженерик-типе {@code P}.
     *
     * <p>Назначение: Используется для прикрепления обработчиков к source.</p>
     */
    protected abstract P self();

    /**
     * Конструктор:
     * <ul>
     *     <li>Выполняет базовые проверки параметров;</li>
     *     <li>Собирает неизменяемую карту "код инструмента → обработчик инструмента";</li>
     *     <li>Валидирует инварианты: уникальность кодов обработчиков, один код инструмента → один обработчик;</li>
     *     <li>Прикрепляет переданные обработчики к source.</li>
     * </ul>
     */
    protected AbstractMarketDataSource(
            ProviderCode providerCode,
            ProviderPassport passport,
            ProviderPolicy policy,
            Set<? extends InstrumentHandler<P, ? extends Instrument>> handlers
    ) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(policy, "policy must not be null");
        Objects.requireNonNull(handlers, "handlers must not be null");

        if (handlers.isEmpty()) {
            throw new IllegalArgumentException("handlers must not be empty");
        }

        this.providerCode = providerCode;
        this.passport = passport;
        this.policy = policy;

        // Собираем неизменяемую однозначную карту "код инструмента → обработчик инструмента"
        this.instrumentHandlerMap = buildInstrumentHandlerMap(providerCode, handlers);

        // Прикрепляем обработчики к source
        for (InstrumentHandler<P, ? extends Instrument> h : handlers) {
            h.attachTo(self());
        }
    }

    @Override
    public ProviderCode providerCode() {
        return providerCode;
    }

    @Override
    public ProviderPassport passport() {
        return passport;
    }

    @Override
    public ProviderPolicy policy() {
        return policy;
    }

    /**
     * Template implementation for streaming source-level ticks:
     * finds the handler for the given instrument and delegates the call to it.
     */
    @Override
    public final <I extends Instrument> Publisher<SourceMarketDataTick> streamSourceTicks(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        // Находим обработчик (или бросаем исключение)
        InstrumentHandler<P, I> handler = findHandlerOrThrow(instrument);

        // Делегируем запрос source ticks обработчику
        return handler.streamSourceTicks(instrument);
    }

    /**
     * Собирает неизменяемую карту и валидирует инварианты:
     * <ul>
     *     <li>Уникальность кодов обработчиков;</li>
     *     <li>Один код инструмента → один обработчик (однозначность).</li>
     * </ul>
     *
     * @param providerCode код source, к которому прикреплены обработчики
     * @param handlers     набор обработчиков
     * @return неизменяемую карту
     */
    private static <P extends MarketDataSource> Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>>
    buildInstrumentHandlerMap(
            ProviderCode providerCode,
            Set<? extends InstrumentHandler<P, ? extends Instrument>> handlers
    ) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(handlers, "handlers must not be null");

        Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>> map = new LinkedHashMap<>();
        Set<HandlerCode> handlerCodes = new HashSet<>();

        for (InstrumentHandler<P, ? extends Instrument> h : handlers) {
            Objects.requireNonNull(h, "handler must not be null");

            HandlerCode handlerCode = Objects.requireNonNull(h.handlerCode(), "handlerCode must not be null");
            if (!handlerCodes.add(handlerCode)) {
                throw new IllegalStateException("Provider '" + providerCode.value() +
                        "' contains multiple handlers with the same code '" + handlerCode.value() + "'");
            }

            Set<InstrumentCode> codes = Objects.requireNonNull(h.supportedInstrumentCodes(),
                    "supportedInstrumentCodes must not be null");

            for (InstrumentCode instrumentCode : codes) {
                Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");

                InstrumentHandler<P, ? extends Instrument> prev = map.putIfAbsent(instrumentCode, h);
                if (prev != null) {
                    throw new IllegalStateException("Provider '" + providerCode.value() +
                            "' contains instrument code '" + instrumentCode.value() +
                            "' that is supported by multiple handlers ('" + prev.handlerCode().value() +
                            "', '" + handlerCode.value() + "')");
                }
            }
        }

        return Collections.unmodifiableMap(map);
    }

    /**
     * Ищет обработчик инструмента или бросает исключение.
     */
    @SuppressWarnings("unchecked")
    protected final <I extends Instrument> InstrumentHandler<P, I> findHandlerOrThrow(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        InstrumentCode code = Objects.requireNonNull(instrument.instrumentCode(),
                "instrumentCode must not be null");

        InstrumentHandler<P, I> handler =
                (InstrumentHandler<P, I>) instrumentHandlerMap.get(code);

        if (handler == null) {
            throw new HandlerNotFoundException(code, providerCode);
        }

        return handler;
    }
}
