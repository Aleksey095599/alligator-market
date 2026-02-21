package com.alligator.market.domain.provider.model;

import com.alligator.market.domain.instrument.model.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.exception.HandlerNotFoundException;
import com.alligator.market.domain.provider.model.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.provider.model.handler.InstrumentHandler;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.policy.ProviderPolicy;
import com.alligator.market.domain.provider.model.vo.HandlerCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.*;

/**
 * Абстрактная реализация {@link MarketDataProvider}.
 */
public abstract non-sealed class AbstractMarketDataProvider<P extends MarketDataProvider>
        implements MarketDataProvider {

    //=================================================================================================================
    // ТОЧКИ РАСШИРЕНИЯ ДЛЯ НАСЛЕДНИКОВ
    //=================================================================================================================

    /* Код провайдера. */
    protected final ProviderCode providerCode;

    /* Паспорт провайдера. */
    protected final ProviderPassport passport;

    /* Политика провайдера. */
    protected final ProviderPolicy policy;

    /* Карта "код инструмента → обработчик инструмента". */
    private final Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>> instrumentHandlerMap;

    //=================================================================================================================
    // КОНСТРУКТОР
    //=================================================================================================================

    /**
     * Конструктор.
     *
     * <p>Собирает объект из переданных параметров и выполняет следующие действия:</p>
     * <ul>
     *     <li>1) Собирает неизменяемую карту "код инструмента → обработчик инструмента".</li>
     *     <li>2) Валидирует инварианты: уникальность кодов обработчиков, один код инструмента → один обработчик.</li>
     *     <li>3) Прикрепляет переданные обработчики к провайдеру.</li>
     * </ul>
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

        // Собираем неизменяемую карту и валидируем инварианты
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

        // Находим обработчик (или бросаем исключение)
        InstrumentHandler<P, I> handler = findHandlerOrThrow(instrument);

        // Делегируем вызов обработчику
        return handler.quote(instrument);
    }

    /* Ищет обработчик инструмента или бросает исключение. */
    @SuppressWarnings("unchecked")
    protected final <I extends Instrument> InstrumentHandler<P, I> findHandlerOrThrow(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        InstrumentCode code = Objects.requireNonNull(
                instrument.instrumentCode(),
                "instrumentCode must not be null"
        );

        InstrumentHandler<P, I> handler =
                (InstrumentHandler<P, I>) instrumentHandlerMap.get(code);

        if (handler == null) {
            throw new HandlerNotFoundException(code, providerCode);
        }

        return handler;
    }

    //=================================================================================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    //=================================================================================================================

    /**
     * Собирает неизменяемую карту и валидирует инварианты:
     * <ul>
     *     <li>Уникальность кодов обработчиков;</li>
     *     <li>Один код инструмента → один обработчик.</li>
     * </ul>
     *
     * @param providerCode код провайдера, к которому прикреплены обработчики
     * @param handlers     набор обработчиков
     * @return неизменяемую карту
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
