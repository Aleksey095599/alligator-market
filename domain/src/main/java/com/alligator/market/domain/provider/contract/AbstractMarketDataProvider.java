package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.code.InstrumentCode;
import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.provider.contract.handler.InstrumentHandler;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.exception.HandlerNotFoundException;
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

    /* Карта "код инструмента --> обработчик инструмента". */
    private final Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>> instrumentHandlerMap;

    //=================================================================================================================
    // КОНСТРУКТОР
    //=================================================================================================================

    /**
     * Конструктор.
     *
     * <p>Проверяет инварианты, собирает карту "код инструмента --> обработчик", прикрепляет обработчики к провайдеру.</p>
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

        // 1) Проверяем, что по ошибке не переданы дубликаты обработчиков.
        // Критерий проверки - уникальность кода обработчика.
        validateUniqueHandlerCodes(providerCode, handlers);

        // 2) Проверяем, что обработчики не имеют пересечений среди множеств
        //    поддерживаемых ими кодов финансовых инструментов
        validateNoOverlappingInstrumentCodes(providerCode, handlers);

        // 3) Собираем неизменяемую карту "код инструмента --> обработчик".
        //    Важно: благодаря проверкам 1) и 2) карта будет однозначной: одному коду инструмента – один обработчик.
        this.instrumentHandlerMap = buildInstrumentHandlerMap(handlers);

        // 4) Прикрепляем обработчики к провайдеру
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
     * Шаблонная реализация котировки: находит обработчик по коду инструмента и делегирует ему вызов.
     *
     * @param instrument инструмент, для которого требуется котировка
     * @return поток котировок (возможен Mono как частный случай)
     */
    @Override
    public final <I extends Instrument> Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        InstrumentHandler<P, I> handler = handlerOf(instrument);
        if (handler == null) {
            throw new HandlerNotFoundException(instrument.instrumentCode(), providerCode);
        }
        return handler.quote(instrument);
    }

    //=================================================================================================================
    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ
    //=================================================================================================================

    /**
     * Проверяет уникальность обработчиков по их коду {@link InstrumentHandler#handlerCode()}.
     *
     * <p>Предполагается что набор обработчиков передается вместе с кодом провайдера {@code providerCode},
     * к которому они принадлежат для вывода корректного сообщения об ошибке.</p>
     *
     * @param providerCode код провайдера, к которому принадлежат обработчики
     * @param handlers     набор обработчиков для проверки
     */
    private static <P extends MarketDataProvider> void validateUniqueHandlerCodes(
            ProviderCode providerCode,
            Set<? extends AbstractInstrumentHandler<P, ? extends Instrument>> handlers
    ) {
        Set<String> handlerCodes = new HashSet<>();
        for (AbstractInstrumentHandler<P, ? extends Instrument> h : handlers) {
            String code = h.handlerCode();
            if (!handlerCodes.add(code)) {
                throw new IllegalStateException("Provider '" + providerCode.value() +
                        "' contains multiple handlers with the same code '" + code + "'");
            }
        }
    }

    /**
     * Проверяет, что переданные обработчики не имеют пересечений среди множеств поддерживаемых ими кодов
     * финансовых инструментов {@link InstrumentHandler#supportedInstrumentCodes()}.
     * Проще говоря, одному коду инструмента – один обработчик.
     *
     * <p>Предполагается что набор обработчиков передается вместе с кодом провайдера {@code providerCode},
     * к которому они принадлежат для вывода корректного сообщения об ошибке.</p>
     *
     * @param providerCode код провайдера, к которому принадлежат обработчики
     * @param handlers     набор обработчиков для проверки
     */
    private static <P extends MarketDataProvider> void validateNoOverlappingInstrumentCodes(
            ProviderCode providerCode,
            Set<? extends AbstractInstrumentHandler<P, ? extends Instrument>> handlers
    ) {
        Map<InstrumentCode, String> ownerByInstrumentCode = new HashMap<>();

        for (AbstractInstrumentHandler<P, ? extends Instrument> h : handlers) {
            String handlerCode = h.handlerCode();

            for (InstrumentCode instrumentCode : h.supportedInstrumentCodes()) {
                String prevOwner = ownerByInstrumentCode.putIfAbsent(instrumentCode, handlerCode);
                if (prevOwner != null && !prevOwner.equals(handlerCode)) {
                    throw new IllegalStateException("Provider '" + providerCode.value() +
                            "' contains instrument code '" + instrumentCode.value() +
                            "' that is supported by multiple handlers ('" + prevOwner +
                            "', '" + handlerCode + "')");
                }
            }
        }
    }

    /**
     * Собирает неизменяемую карту "код инструмента --> обработчик".
     *
     * @param handlers набор обработчиков для сборки карты
     * @return неизменяемая однозначная карта "код инструмента --> обработчик"
     */
    private static <P extends MarketDataProvider> Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>>
    buildInstrumentHandlerMap(
            Set<? extends AbstractInstrumentHandler<P, ? extends Instrument>> handlers
    ) {
        Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>> map = new LinkedHashMap<>();

        for (AbstractInstrumentHandler<P, ? extends Instrument> h : handlers) {
            for (InstrumentCode code : h.supportedInstrumentCodes()) {
                map.put(code, h);
            }
        }
        return Collections.unmodifiableMap(map);
    }

    /**
     * F-bounded полиморфизм: возвращает текущий экземпляр провайдера в его конкретном дженерик-типе {@code P}.
     */
    protected abstract P self();

    /**
     * Возвращает обработчик по коду инструмента.
     */
    @SuppressWarnings("unchecked")
    private <I extends Instrument> InstrumentHandler<P, I> handlerOf(I instrument) {
        InstrumentCode code = instrument.instrumentCode();
        if (code == null) {
            return null;
        }
        InstrumentHandler<P, ? extends Instrument> h = instrumentHandlerMap.get(code);
        return (InstrumentHandler<P, I>) h;
    }
}
