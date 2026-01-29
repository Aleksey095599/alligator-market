package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.model.handler.model.AbstractInstrumentHandler;
import com.alligator.market.domain.provider.model.handler.model.InstrumentHandler;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.policy.ProviderPolicy;
import com.alligator.market.domain.provider.exception.HandlerNotFoundException;
import com.alligator.market.domain.provider.model.vo.HandlerCode;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.*;

/**
 * Абстрактная реализация провайдера рыночных данных {@link MarketDataProvider}.
 */
public abstract non-sealed class AbstractMarketDataProvider<P extends MarketDataProvider>
        implements MarketDataProvider {

    /* Код провайдера. */
    protected final ProviderCode providerCode;

    /* Паспорт провайдера. */
    protected final ProviderPassport passport;

    /* Политика провайдера. */
    protected final ProviderPolicy policy;

    /* Карта "код инструмента → обработчик инструмента" (далее карта). */
    private final Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>> instrumentHandlerMap;

    //=================================================================================================================
    // КОНСТРУКТОР
    //=================================================================================================================

    /**
     * Конструктор: собирает карту, валидирует инварианты, прикрепляет обработчики к провайдеру.</p>
     */
    protected AbstractMarketDataProvider(
            ProviderCode providerCode,
            ProviderPassport passport,
            ProviderPolicy policy,
            Set<? extends AbstractInstrumentHandler<P, ? extends Instrument>> handlers
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

        // Собираем неизменяемую однозначную карту и валидируем инварианты
        this.instrumentHandlerMap = buildInstrumentHandlerMap(providerCode, handlers);

        // Прикрепляем обработчики к провайдеру
        for (AbstractInstrumentHandler<P, ? extends Instrument> h : handlers) {
            h.attachTo(self());
        }
    }

    //=================================================================================================================
    // РЕАЛИЗАЦИЯ МЕТОДОВ КОНТРАКТА
    //=================================================================================================================

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
     * Шаблонная реализация получения потока котировок:
     * с помощью карты находит обработчик для заданного инструмента и делегирует ему вызов.
     *
     * @param instrument инструмент, для которого требуется котировка
     * @return поток котировок (возможен Mono как частный случай)
     */
    @Override
    public final <I extends Instrument> Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        // Извлекаем код инструмента
        InstrumentCode code = Objects.requireNonNull(instrument.instrumentCode(),
                "instrumentCode must not be null");

        // Используем карту для поиска обработчика
        @SuppressWarnings("unchecked")
        InstrumentHandler<P, I> handler = (InstrumentHandler<P, I>) instrumentHandlerMap.get(code);

        // Если обработчик не найден, значит инструмент не поддерживается провайдером
        if (handler == null) {
            throw new HandlerNotFoundException(code, providerCode);
        }

        // Делегируем вызов обработчику
        return handler.quote(instrument);
    }

    //=================================================================================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    //=================================================================================================================

    /**
     * Собирает неизменяемую однозначную карту и валидирует инварианты:
     * <ul>
     *     <li>Коды обработчиков {@link InstrumentHandler#handlerCode()} уникальны;</li>
     *     <li>Одному {@link InstrumentCode} соответствует ровно один обработчик.</li>
     * </ul>
     *
     * @param providerCode код провайдера, к которому прикреплены обработчики
     * @param handlers     набор обработчиков
     * @return неизменяемую однозначную карту
     */
    private static <P extends MarketDataProvider> Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>>
    buildInstrumentHandlerMap(
            ProviderCode providerCode,
            Set<? extends AbstractInstrumentHandler<P, ? extends Instrument>> handlers
    ) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(handlers, "handlers must not be null");

        Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>> map = new LinkedHashMap<>();
        Set<HandlerCode> handlerCodes = new HashSet<>();

        for (AbstractInstrumentHandler<P, ? extends Instrument> h : handlers) {
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
     * F-bounded полиморфизм: возвращает текущий экземпляр провайдера в его конкретном дженерик-типе {@code P}.
     */
    protected abstract P self();
}
