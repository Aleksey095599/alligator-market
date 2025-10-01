package com.alligator.market.domain.provider.contract;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.handler.AbstractInstrumentHandler;
import com.alligator.market.domain.provider.contract.handler.InstrumentHandler;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import com.alligator.market.domain.provider.contract.settings.ProviderSettings;
import com.alligator.market.domain.provider.exception.HandlerNotFoundException;
import com.alligator.market.domain.quote.QuoteTick;
import org.reactivestreams.Publisher;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Абстрактный каркас провайдера рыночных данных.
 */
public abstract non-sealed class AbstractMarketDataProvider<P extends MarketDataProvider>
        implements MarketDataProvider {

    /* ↓↓ Базовые атрибуты провайдера. */
    protected final String providerCode;
    protected final ProviderDescriptor descriptor;
    protected final ProviderPolicy policy;
    private final AtomicReference<ProviderSettings> settingsRef;

    /* Карта "инструмент → обработчик". После инициализации становится неизменяемой. */
    private final Map<Instrument, InstrumentHandler<P, ? extends Instrument>> instrumentMap;

    /* ↓↓ Коллекции для кодов и типов инструментов. После инициализации становятся неизменяемыми. */
    private final Set<String> instrumentCodes;
    private final Set<InstrumentType> instrumentTypes;

    /* F-bounded полиморфизм: даём наследникам вернуть "себя" нужного типа. */
    protected abstract P self();

    /**
     * Конструктор.
     *
     * @throws NullPointerException     если переданы null-дескриптор или набор обработчиков
     * @throws IllegalArgumentException если набор обработчиков пуст
     * @throws IllegalStateException    если обнаружены дубликаты обработчиков или инструментов,
     *                                  если инструмент не соответствует декларируемому в обработчике классу
     */
    protected AbstractMarketDataProvider(
            String providerCode,
            String displayName,
            ProviderDescriptor descriptor,
            ProviderPolicy policy,
            ProviderSettings settings,
            Set<? extends AbstractInstrumentHandler<P, ? extends Instrument>> handlers // Набор обработчиков
    ) {
        // ↓↓ Базовая валидация аргументов
        Objects.requireNonNull(providerCode,"providerCode must not be null");
        Objects.requireNonNull(displayName,"displayName must not be null");
        Objects.requireNonNull(descriptor,"descriptor must not be null");
        Objects.requireNonNull(policy,"policy must not be null");
        Objects.requireNonNull(settings,"settings must not be null");
        Objects.requireNonNull(handlers,"handlers must not be null");
        if (providerCode.isBlank()) {
            throw new IllegalArgumentException("providerCode must not be blank");
        }
        if (providerCode.chars().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("providerCode must not contain whitespaces");
        }
        if (!providerCode.equals(providerCode.toUpperCase(Locale.ROOT))) {
            throw new IllegalArgumentException("providerCode must be upper case");
        }
        if (displayName.isBlank()) {
            throw new IllegalArgumentException("displayName must not be blank");
        }
        if (handlers.isEmpty()) {
            throw new IllegalArgumentException("handlers must not be empty");
        }

        // ↓↓ Инициализация базовых атрибутов провайдера
        this.providerCode = providerCode.trim();
        this.descriptor   = descriptor;
        this.policy       = policy;
        this.settingsRef  = new AtomicReference<>(settings);

        // 1) Проверяем уникальность handlers по коду
        var handlerCodes = new java.util.HashSet<String>();
        for (var h : handlers) {
            var code = h.handlerCode();
            if (!handlerCodes.add(code)) {
                throw new IllegalStateException("Provider '" + providerCode +
                        "' contains multiple handlers with the same code '" + code + "'");
            }
        }

        // 2) Собираем карту "инструмент → обработчик"
        var map = new java.util.LinkedHashMap<Instrument, InstrumentHandler<P, ? extends Instrument>>();
        for (var h : handlers) {
            var supported = h.supportedInstruments();
            for (Instrument ins : supported) {
                // ↓↓ Дополнительно проверяем, что каждый инструмент соответствует декларируемому в обработчике классу
                if (!h.instrumentClass().isInstance(ins)) {
                    throw new IllegalStateException(
                            "Instrument '" + ins.code() +
                                    "' must match '" + h.instrumentClass().getSimpleName()
                                    + "' for handler '" + h.handlerCode() + "'"
                    );
                }
                // ↓↓ Обеспечиваем уникальность ключей (инструментов) в карте при сборке
                var prev = map.putIfAbsent(ins, h);
                if (prev != null) {
                    throw new IllegalStateException("Instrument '" + ins.code() +
                            "' is already bound to handler '" + prev.handlerCode() +
                            "' in provider '" + providerCode + "'");
                }
            }
        }

        // 3) Извлекаем набор кодов и типов инструментов из карты
        var instrumentCodes = new java.util.LinkedHashSet<String>();
        var instrumentTypes = java.util.EnumSet.noneOf(InstrumentType.class);
        for (Instrument instrument : map.keySet()) {
            instrumentCodes.add(instrument.code());
            instrumentTypes.add(instrument.type());
        }

        // 4) Фиксируем структуру неизменяемых коллекций
        this.instrumentMap = java.util.Collections.unmodifiableMap(map);
        this.instrumentCodes = java.util.Collections.unmodifiableSet(instrumentCodes);
        this.instrumentTypes = java.util.Collections.unmodifiableSet(instrumentTypes);

        // 5) Извлекаем набор неповторяющихся обработчиков из фиксированной карты
        var handlersFromMap = new java.util.LinkedHashSet<>(map.values());

        // (!) Мы умышленно сперва "разложили" переданный в конструктор набор обработчиков по инструментам,
        // создали и зафиксировали карту, а уже затем из зафиксированной карты собрали обратно набор обработчиков.
        // Это гарантирует безопасную инициализацию: обработчики получают ссылку на провайдера только после того,
        // как все инварианты проверены и структура реестра окончательно зафиксирована.

        // 6) Перебираем обработчики и прикрепляем их к провайдеру
        for (var h : handlersFromMap) {
            h.attachTo(self()); // ← attachTo должен только сохранить ссылку и ничего не вызывать
        }
    }

    /** Технический код провайдера. */
    @Override
    public String providerCode() {
        return providerCode;
    }

    /** Дескриптор провайдера: иммутабельный набор статических атрибутов (только отображение). */
    @Override
    public ProviderDescriptor descriptor() {
        return descriptor;
    }

    /** Политика провайдера: иммутабельные параметры, которые использует бизнес-логика. */
    public ProviderPolicy policy() {
        return policy;
    }

    /** Настройки провайдера: параметры, которые разрешено менять из frontend. */
    @Override
    public ProviderSettings settings() {
        return settingsRef.get();
    }

    /** Иммутабельный набор кодов поддерживаемых провайдером инструментов. */
    @Override
    public Set<String> instrumentsCodes() {
        return instrumentCodes;
    }

    /** Иммутабельный набор типов поддерживаемых провайдером инструментов. */
    @Override
    public Set<InstrumentType> instrumentsTypes() {
        return instrumentTypes;
    }

    /**
     * Унифицированная операция получения котировок для любого поддерживаемого инструмента.
     *
     * @throws NullPointerException     если передан null-инструмент
     * @throws HandlerNotFoundException если обработчик для инструмента не зарегистрирован
     */
    @Override
    public final <I extends Instrument> Publisher<QuoteTick> quote(I instrument) {
        Objects.requireNonNull(instrument, "instrument must not be null");
        var handler = handlerOf(instrument);
        if (handler == null) {
            throw new HandlerNotFoundException(instrument.code(), providerCode);
        }
        return handler.quote(instrument);
    }

    /**
     * Атомарно применяет новые настройки. Только для наследников, переопределять нельзя.
     */
    @SuppressWarnings("unused")
    protected final void replaceSettings(ProviderSettings newSettings) {
        Objects.requireNonNull(newSettings, "newSettings must not be null");
        settingsRef.set(newSettings);
    }

    /** Мост типов для извлечения обработчика под конкретный инструмент. */
    @SuppressWarnings("unchecked")
    private <I extends Instrument> InstrumentHandler<P, I> handlerOf(I instrument) {
        return (InstrumentHandler<P, I>) instrumentMap.get(instrument);
    }
}
