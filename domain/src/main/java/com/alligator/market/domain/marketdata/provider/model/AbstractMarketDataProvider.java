package com.alligator.market.domain.marketdata.provider.model;

import com.alligator.market.domain.instrument.base.model.Instrument;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.marketdata.provider.model.handler.exception.HandlerNotFoundException;
import com.alligator.market.domain.marketdata.provider.model.handler.InstrumentHandler;
import com.alligator.market.domain.marketdata.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.marketdata.provider.model.policy.ProviderPolicy;
import com.alligator.market.domain.marketdata.provider.model.vo.HandlerCode;
import com.alligator.market.domain.marketdata.provider.model.vo.ProviderCode;
import com.alligator.market.domain.quote.response.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.*;

/**
 * Абстрактная реализация {@link MarketDataProvider}.
 */
public abstract class AbstractMarketDataProvider<P extends MarketDataProvider>
        implements MarketDataProvider {

    //region FIELDS

    /* Код провайдера. */
    protected final ProviderCode providerCode;

    /* Паспорт провайдера. */
    protected final ProviderPassport passport;

    /* Политика провайдера. */
    protected final ProviderPolicy policy;

    /* Карта "код инструмента → обработчик инструмента". */
    private final Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>> instrumentHandlerMap;

    //endregion

    //region EXTENSION POINT

    /**
     * F-bounded полиморфизм: возвращает текущий экземпляр провайдера в его конкретном дженерик-типе {@code P}.
     *
     * <p>Назначение: Используется для прикрепления обработчиков к провайдеру.</p>
     */
    protected abstract P self();

    //endregion

    //region CONSTRUCTION

    /**
     * Конструктор.
     *
     * <p>Собирает объект из переданных параметров и выполняет следующие действия:</p>
     * <ul>
     *     <li>Выполняет базовые проверки параметров;</li>
     *     <li>Собирает неизменяемую карту "код инструмента → обработчик инструмента";</li>
     *     <li>Валидирует инварианты: уникальность кодов обработчиков, один код инструмента → один обработчик;</li>
     *     <li>Прикрепляет переданные обработчики к провайдеру.</li>
     * </ul>
     */
    protected AbstractMarketDataProvider(
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

        // Собираем неизменяемую карту и валидируем инварианты
        this.instrumentHandlerMap = buildInstrumentHandlerMap(providerCode, handlers);

        // Прикрепляем обработчики к провайдеру
        for (InstrumentHandler<P, ? extends Instrument> h : handlers) {
            h.attachTo(self());
        }
    }

    //endregion

    //region PUBLIC API

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
     * Шаблонная реализация получения потока котировок: с помощью карты находит обработчик для заданного
     * инструмента и делегирует ему вызов.
     */
    @Override
    public final <I extends Instrument> Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        // Находим обработчик (или бросаем исключение)
        InstrumentHandler<P, I> handler = findHandlerOrThrow(instrument);

        // Делегируем вызов обработчику
        return handler.quote(instrument);
    }

    //endregion

    //region INTERNALS

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

    //endregion
}
