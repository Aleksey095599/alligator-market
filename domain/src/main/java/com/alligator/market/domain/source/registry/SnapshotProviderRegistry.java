package com.alligator.market.domain.source.registry;

import com.alligator.market.domain.source.MarketDataSource;
import com.alligator.market.domain.source.passport.ProviderPassport;
import com.alligator.market.domain.source.passport.vo.ProviderDisplayName;
import com.alligator.market.domain.source.vo.ProviderCode;

import java.util.*;

/**
 * Реализация {@link ProviderRegistry} в виде неизменяемого snapshot набора действующих провайдеров.
 *
 * <p>Инварианты и ограничения, заданные в {@link ProviderRegistry}, валидируются в конструкторе и далее гарантируются
 * на протяжении жизни экземпляра.</p>
 */
public final class SnapshotProviderRegistry implements ProviderRegistry {

    /* Неизменяемая карта "код провайдера → провайдер". */
    private final Map<ProviderCode, MarketDataSource> providersByCode;

    /* Неизменяемая карта "код провайдера → паспорт провайдера". */
    private final Map<ProviderCode, ProviderPassport> passportsByCode;

    /**
     * Конструктор snapshot-реестра.
     *
     * <p>Собирает неизменяемые карты и валидирует инварианты заданные для {@link ProviderRegistry}.</p>
     *
     * @param providers список провайдеров
     * @throws IllegalArgumentException если список провайдеров пуст или содержит дублирующиеся данные
     */
    public SnapshotProviderRegistry(List<? extends MarketDataSource> providers) {
        Objects.requireNonNull(providers, "providers must not be null");

        if (providers.isEmpty()) {
            throw new IllegalArgumentException("Provider registry must contain at least one provider");
        }

        Map<ProviderCode, MarketDataSource> providersMap = new LinkedHashMap<>();
        Map<ProviderCode, ProviderPassport> passportsMap = new LinkedHashMap<>();
        Set<String> displayNamesLower = new HashSet<>();

        for (MarketDataSource source : providers) {
            Objects.requireNonNull(source, "source must not be null");

            ProviderCode code = Objects.requireNonNull(source.providerCode(),
                    "source.providerCode must not be null");

            ProviderPassport passport = Objects.requireNonNull(source.passport(),
                    "source.passport must not be null");

            Objects.requireNonNull(source.policy(),
                    "source.policy must not be null");

            // Уникальность кода провайдера
            MarketDataSource prev = providersMap.put(code, source);
            if (prev != null) {
                throw new IllegalArgumentException(
                        "Duplicate provider code detected (code=" + code.value() + ")"
                );
            }

            ProviderDisplayName displayName = Objects.requireNonNull(passport.displayName(),
                    "provider.passport.displayName must not be null");

            // Уникальность displayName без учёта регистра
            String displayNameValue = displayName.value();
            String displayNameLower = displayNameValue.toLowerCase(Locale.ROOT);
            if (!displayNamesLower.add(displayNameLower)) {
                throw new IllegalArgumentException(
                        "Duplicate provider display name detected (displayName=" + displayNameValue + ")"
                );
            }

            passportsMap.put(code, passport);
        }

        this.providersByCode = Collections.unmodifiableMap(providersMap);
        this.passportsByCode = Collections.unmodifiableMap(passportsMap);
    }

    @Override
    public Map<ProviderCode, MarketDataSource> providersByCode() {
        return providersByCode;
    }

    /**
     * Замена дефолтной реализации: возвращается карта, созданная в конструкторе (более эффективно).
     */
    @Override
    public Map<ProviderCode, ProviderPassport> passportsByCode() {
        return passportsByCode;
    }
}
