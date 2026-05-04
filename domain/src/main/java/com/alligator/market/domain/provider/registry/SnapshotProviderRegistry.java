package com.alligator.market.domain.provider.registry;

import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.passport.ProviderPassport;
import com.alligator.market.domain.provider.vo.ProviderCode;

import java.util.*;

/**
 * Реализация {@link ProviderRegistry} в виде неизменяемого snapshot набора действующих провайдеров.
 *
 * <p>Инварианты и ограничения, заданные в {@link ProviderRegistry}, валидируются в конструкторе и далее гарантируются
 * на протяжении жизни экземпляра.</p>
 */
public final class SnapshotProviderRegistry implements ProviderRegistry {

    /* Неизменяемая карта "код провайдера → провайдер". */
    private final Map<ProviderCode, MarketDataProvider> providersByCode;

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
    public SnapshotProviderRegistry(List<? extends MarketDataProvider> providers) {
        Objects.requireNonNull(providers, "providers must not be null");

        if (providers.isEmpty()) {
            throw new IllegalArgumentException("Provider registry must contain at least one provider");
        }

        Map<ProviderCode, MarketDataProvider> providersMap = new LinkedHashMap<>();
        Map<ProviderCode, ProviderPassport> passportsMap = new LinkedHashMap<>();
        Set<String> displayNamesLower = new HashSet<>();

        for (MarketDataProvider provider : providers) {
            Objects.requireNonNull(provider, "provider must not be null");

            ProviderCode code = Objects.requireNonNull(provider.providerCode(),
                    "provider.providerCode must not be null");

            ProviderPassport passport = Objects.requireNonNull(provider.passport(),
                    "provider.passport must not be null");

            Objects.requireNonNull(provider.policy(),
                    "provider.policy must not be null");

            // Уникальность кода провайдера
            MarketDataProvider prev = providersMap.put(code, provider);
            if (prev != null) {
                throw new IllegalArgumentException(
                        "Duplicate provider code detected (code=" + code.value() + ")"
                );
            }

            String displayName = Objects.requireNonNull(passport.displayName(),
                    "provider.passport.displayName must not be null");

            // Уникальность displayName без учёта регистра
            String displayNameLower = displayName.toLowerCase(Locale.ROOT);
            if (!displayNamesLower.add(displayNameLower)) {
                throw new IllegalArgumentException(
                        "Duplicate provider display name detected (displayName=" + displayName + ")"
                );
            }

            passportsMap.put(code, passport);
        }

        this.providersByCode = Collections.unmodifiableMap(providersMap);
        this.passportsByCode = Collections.unmodifiableMap(passportsMap);
    }

    @Override
    public Map<ProviderCode, MarketDataProvider> providersByCode() {
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
