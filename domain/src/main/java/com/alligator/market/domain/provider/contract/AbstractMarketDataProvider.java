package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.code.InstrumentCode;
import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.provider.contract.handler.InstrumentHandler;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.contract.settings.ProviderSettings;
import com.alligator.market.domain.provider.exception.HandlerNotFoundException;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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

    /* Настройки провайдера. */
    private final AtomicReference<ProviderSettings> settingsRef;

    /* Карта "код инструмента --> обработчик". После инициализации становится неизменяемой. */
    private final Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>> instrumentMapByCode;

    /* Коллекции для кодов и типов инструментов. После инициализации становятся неизменяемыми. */
    private final Set<InstrumentCode> instrumentCodes;
    private final Set<InstrumentType> instrumentTypes;

    //=================================================================================================================
    // КОНСТРУКТОР
    //=================================================================================================================

    /**
     * Конструктор.
     *
     * <p>Проверяет инварианты, нормализует код провайдера, собирает карту "код инструмента --> обработчик",
     * прикрепляет обработчики к провайдеру.</p>
     *
     * @param providerCode код провайдера
     * @param passport     паспорт провайдера
     * @param policy       политика провайдера
     * @param settings     настройки провайдера
     * @throws NullPointerException     если переданы null-аргументы
     * @throws IllegalArgumentException если передан blank код провайдера или пустой набор обработчиков
     * @throws IllegalStateException    если обнаружены дубликаты обработчиков по коду или пересечения кодов инструментов
     */
    protected AbstractMarketDataProvider(
            ProviderCode providerCode,
            ProviderPassport passport,
            ProviderPolicy policy,
            ProviderSettings settings,
            Set<? extends AbstractInstrumentHandler<P, ? extends Instrument>> handlers // Набор обработчиков
    ) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(passport, "passport must not be null");
        Objects.requireNonNull(policy, "policy must not be null");
        Objects.requireNonNull(settings, "settings must not be null");
        Objects.requireNonNull(handlers, "handlers must not be null");

        if (handlers.isEmpty()) {
            throw new IllegalArgumentException("handlers must not be empty");
        }

        this.providerCode = providerCode;
        this.passport = passport;
        this.policy = policy;
        this.settingsRef = new AtomicReference<>(settings);

        // 1) Проверяем уникальность handlers по коду
        Set<String> handlerCodes = new HashSet<>();
        for (AbstractInstrumentHandler<P, ? extends Instrument> h : handlers) {
            String code = h.handlerCode();
            if (!handlerCodes.add(code)) {
                throw new IllegalStateException("Provider '" + providerCode.value() +
                        "' contains multiple handlers with the same code '" + code + "'");
            }
        }

        // 2) Собираем карту "код инструмента --> обработчик" и агрегируем наборы кодов/типов
        Map<InstrumentCode, InstrumentHandler<P, ? extends Instrument>> mapByCode =
                new LinkedHashMap<>(); // <-- Карта
        Set<InstrumentCode> allCodes = new LinkedHashSet<>(); // <-- Набор всех кодов инструментов
        Set<InstrumentType> types = EnumSet.noneOf(InstrumentType.class); // <-- Набор всех типов инструментов
        for (AbstractInstrumentHandler<P, ? extends Instrument> h : handlers) {
            types.add(h.instrumentType()); // <-- Один тип на обработчик
            for (InstrumentCode code : h.supportedInstrumentCodes()) {
                InstrumentHandler<P, ? extends Instrument> prev = mapByCode.putIfAbsent(code, h);
                if (prev != null && prev != h) {
                    // Запрещаем связывать один и тот же код с разными обработчиками
                    throw new IllegalStateException("Instrument code '" + code.value() +
                            "' is already bound to handler '" + prev.handlerCode() +
                            "' in provider '" + providerCode.value() + "'");
                }
                allCodes.add(code);
            }
        }

        // 3) Фиксируем структуру неизменяемых коллекций
        this.instrumentMapByCode = Collections.unmodifiableMap(mapByCode);
        this.instrumentCodes = Collections.unmodifiableSet(allCodes);
        this.instrumentTypes = Collections.unmodifiableSet(types);

        // 4) Прикрепляем обработчики к провайдеру
        Set<AbstractInstrumentHandler<P, ? extends Instrument>> uniqueHandlers =
                new LinkedHashSet<>(handlers);
        for (AbstractInstrumentHandler<P, ? extends Instrument> h : uniqueHandlers) {
            h.attachTo(self()); // <-- attachTo должен только сохранить ссылку и ничего не вызывать
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

    public ProviderPolicy policy() {
        return policy;
    }

    @Override
    public ProviderSettings settings() {
        return settingsRef.get();
    }

    @Override
    public Set<InstrumentCode> instrumentsCodes() {
        return instrumentCodes;
    }

    @Override
    public Set<InstrumentType> instrumentsTypes() {
        return instrumentTypes;
    }

    /**
     * Шаблонная реализация котировки: находит обработчик по коду инструмента и делегирует ему вызов.
     *
     * @param instrument инструмент, для которого требуется котировка
     * @return поток котировок (возможен Mono как частный случай)
     * @throws NullPointerException     если {@code instrument} равен {@code null}
     * @throws HandlerNotFoundException если обработчик для кода инструмента не зарегистрирован у провайдера
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
     * F-bounded полиморфизм: возвращает текущий экземпляр провайдера в его конкретном дженерик-типе {@code P}.
     */
    protected abstract P self();

    /**
     * Атомарно заменяет настройки провайдера.
     *
     * <p>Метод защищённый и финальный, предназначен для использования наследниками.</p>
     *
     * @param newSettings новые настройки
     * @throws NullPointerException если {@code newSettings} равен {@code null}
     */
    @SuppressWarnings("unused")
    protected final void replaceSettings(ProviderSettings newSettings) {
        Objects.requireNonNull(newSettings, "newSettings must not be null");
        settingsRef.set(newSettings);
    }

    /**
     * Возвращает обработчик по коду инструмента.
     */
    @SuppressWarnings("unchecked")
    private <I extends Instrument> InstrumentHandler<P, I> handlerOf(I instrument) {
        InstrumentCode code = instrument.instrumentCode();
        if (code == null) {
            return null;
        }
        InstrumentHandler<P, ? extends Instrument> h = instrumentMapByCode.get(code);
        return (InstrumentHandler<P, I>) h;
    }
}
