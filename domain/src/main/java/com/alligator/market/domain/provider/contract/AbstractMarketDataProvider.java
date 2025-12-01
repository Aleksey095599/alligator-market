package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.provider.contract.handler.InstrumentHandler;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.contract.settings.ProviderSettings;
import com.alligator.market.domain.provider.exception.HandlerNotFoundException;
import com.alligator.market.domain.quote.tick.model.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Абстрактный каркас контракта провайдера рыночных данных {@link MarketDataProvider}.
 */
public abstract non-sealed class AbstractMarketDataProvider<P extends MarketDataProvider>
        implements MarketDataProvider {

    /* Нормализованный технический код провайдера: UPPERCASE, формат [A-Z0-9_]+. */
    protected final String normProviderCode;

    /* Дескриптор провайдера: иммутабельный набор статических атрибутов (только отображение). */
    protected final ProviderDescriptor descriptor;

    /* "Политика провайдера": иммутабельные параметры, которые использует бизнес-логика. */
    protected final ProviderPolicy policy;

    /* Настройки провайдера: параметры, которые разрешено менять из frontend. */
    private final AtomicReference<ProviderSettings> settingsRef;

    /* Карта "код инструмента --> обработчик". После инициализации становится неизменяемой. */
    private final Map<String, InstrumentHandler<P, ? extends Instrument>> instrumentMapByCode;

    /* Коллекции для кодов и типов инструментов. После инициализации становятся неизменяемыми. */
    private final Set<String> instrumentCodes;
    private final Set<InstrumentType> instrumentTypes;

    //=================================================================================================================
    // КОНСТРУКТОР
    //=================================================================================================================

    /**
     * Конструктор.
     * Проверяет инварианты, нормализует код провайдера, собирает карту "код инструмента --> обработчик",
     * прикрепляет обработчики к провайдеру.
     *
     * @param providerCode код провайдера (UPPERCASE, формат [A-Z0-9_]+)
     * @param descriptor   дескриптор провайдера
     * @param policy       политика провайдера
     * @param settings     настройки провайдера
     * @throws NullPointerException     если переданы null-аргументы
     * @throws IllegalArgumentException если передан blank код провайдера или пустой набор обработчиков
     * @throws IllegalStateException    если обнаружены дубликаты обработчиков по коду или пересечения кодов инструментов
     */
    protected AbstractMarketDataProvider(
            String providerCode,
            ProviderDescriptor descriptor,
            ProviderPolicy policy,
            ProviderSettings settings,
            Set<? extends AbstractInstrumentHandler<P, ? extends Instrument>> handlers // Набор обработчиков
    ) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(descriptor, "descriptor must not be null");
        Objects.requireNonNull(policy, "policy must not be null");
        Objects.requireNonNull(settings, "settings must not be null");
        Objects.requireNonNull(handlers, "handlers must not be null");

        if (providerCode.isBlank()) {
            throw new IllegalArgumentException("providerCode must not be blank");
        }
        if (handlers.isEmpty()) {
            throw new IllegalArgumentException("handlers must not be empty");
        }

        // Инициализация базовых атрибутов провайдера
        this.normProviderCode = normalizeProviderCode(providerCode);
        this.descriptor = descriptor;
        this.policy = policy;
        this.settingsRef = new AtomicReference<>(settings);

        // 1) Проверяем уникальность handlers по коду
        var handlerCodes = new java.util.HashSet<String>();
        for (var h : handlers) {
            var code = h.handlerCode();
            if (!handlerCodes.add(code)) {
                throw new IllegalStateException("Provider '" + providerCode +
                        "' contains multiple handlers with the same code '" + code + "'");
            }
        }

        // 2) Собираем карту "код инструмента --> обработчик" и агрегируем наборы кодов/типов
        var mapByCode = new java.util.LinkedHashMap<String, InstrumentHandler<P, ? extends Instrument>>(); // <-- Карта
        var allCodes = new java.util.LinkedHashSet<String>(); // <-- Набор всех кодов инструментов
        var types = java.util.EnumSet.noneOf(InstrumentType.class); // <-- Набор всех типов инструментов

        for (var h : handlers) {
            types.add(h.instrumentType()); // <-- Один тип на обработчик
            for (String code : h.supportedInstrumentCodes()) {
                // Коды в обработчике уже нормализованы, но повторно нормализуем для надёжности
                var upper = code.toUpperCase(java.util.Locale.ROOT);
                var prev = mapByCode.putIfAbsent(upper, h);
                if (prev != null && prev != h) {
                    // Запрещаем связывать один и тот же код с разными обработчиками
                    throw new IllegalStateException("Instrument code '" + upper +
                            "' is already bound to handler '" + prev.handlerCode() +
                            "' in provider '" + providerCode + "'");
                }
                allCodes.add(upper);
            }
        }

        // 3) Фиксируем структуру неизменяемых коллекций
        this.instrumentMapByCode = java.util.Collections.unmodifiableMap(mapByCode);
        this.instrumentCodes = java.util.Collections.unmodifiableSet(allCodes);
        this.instrumentTypes = java.util.Collections.unmodifiableSet(types);

        // 4) Прикрепляем обработчики к провайдеру
        var uniqueHandlers = new java.util.LinkedHashSet<>(handlers);
        for (var h : uniqueHandlers) {
            h.attachTo(self()); // <-- attachTo должен только сохранить ссылку и ничего не вызывать
        }
    }

    //=================================================================================================================
    // РЕАЛИЗАЦИЯ МЕТОДОВ КОНТРАКТА
    //=================================================================================================================

    /**
     * Технический код провайдера.
     */
    @Override
    public String providerCode() {
        return normProviderCode;
    }

    /**
     * Дескриптор провайдера: иммутабельный набор статических атрибутов (только отображение).
     */
    @Override
    public ProviderDescriptor descriptor() {
        return descriptor;
    }

    /**
     * "Политика провайдера": иммутабельные параметры, которые использует бизнес-логика.
     */
    public ProviderPolicy policy() {
        return policy;
    }

    /**
     * Настройки провайдера: параметры, которые разрешено менять из frontend.
     */
    @Override
    public ProviderSettings settings() {
        return settingsRef.get();
    }

    /**
     * Иммутабельный набор кодов поддерживаемых провайдером инструментов.
     */
    @Override
    public Set<String> instrumentsCodes() {
        return instrumentCodes;
    }

    /**
     * Иммутабельный набор типов поддерживаемых провайдером инструментов.
     */
    @Override
    public Set<InstrumentType> instrumentsTypes() {
        return instrumentTypes;
    }

    /**
     * Шаблонная реализация котировки: находит обработчик по коду инструмента и делегирует вызов.
     *
     * @param instrument инструмент, для которого требуется котировка
     * @return поток котировок
     * @throws NullPointerException     если {@code instrument} равен {@code null}
     * @throws HandlerNotFoundException если обработчик для кода инструмента не зарегистрирован у провайдера
     */
    @Override
    public final <I extends Instrument> Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");

        var handler = handlerOf(instrument);
        if (handler == null) {
            throw new HandlerNotFoundException(instrument.instrumentCode(), normProviderCode);
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

    //=================================================================================================================
    // УТИЛИТЫ
    //=================================================================================================================

    /**
     * Нормализует и валидирует код провайдера: trim --> UPPERCASE --> проверка формата [A-Z0-9_]+.
     *
     * @throws IllegalArgumentException если код провайдера не соответствует формату
     */
    private static String normalizeProviderCode(String code) {
        // Нормализуем код провайдера
        var normalized = code.trim().toUpperCase(java.util.Locale.ROOT);
        // Разрешены только латинские заглавные, цифры и подчёркивание
        if (!normalized.chars().allMatch(ch ->
                (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') || ch == '_')) {
            throw new IllegalArgumentException(
                    "providerCode must match pattern [A-Z0-9_]+: '" + normalized + "'");
        }
        return normalized;
    }

    /**
     * Возвращает обработчик по коду инструмента (код нормализуется в UPPERCASE) или {@code null},
     * если код отсутствует/пустой или не зарегистрирован.
     */
    @SuppressWarnings("unchecked")
    private <I extends Instrument> InstrumentHandler<P, I> handlerOf(I instrument) {
        var raw = instrument.instrumentCode();
        if (raw == null || raw.isBlank()) {
            return null;
        }
        var codeUpper = instrument.instrumentCode().toUpperCase(java.util.Locale.ROOT);
        InstrumentHandler<P, ? extends Instrument> h = instrumentMapByCode.get(codeUpper);
        return (InstrumentHandler<P, I>) h;
    }
}
