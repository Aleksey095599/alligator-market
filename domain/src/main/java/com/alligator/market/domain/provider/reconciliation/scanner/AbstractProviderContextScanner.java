package com.alligator.market.domain.provider.reconciliation.scanner;

import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.exception.ProviderCodeDuplicateException;
import com.alligator.market.domain.provider.exception.ProviderDisplayNameDuplicateException;

import java.util.*;

/**
 * Абстрактный каркас сканера контекста приложения {@link ProviderContextScanner}.
 */
public abstract non-sealed class AbstractProviderContextScanner implements ProviderContextScanner {

    /**
     * Возвращает последовательность провайдеров для построения карты паспортов.
     */
    protected abstract Iterable<MarketDataProvider> providers();

    /**
     * Возвращает карту паспортов провайдеров {@link ProviderPassport}, индексированную по коду.
     * Содержит проверки на дублирование по коду провайдера и отображаемому имени.
     */
    @Override
    public final Map<ProviderCode, ProviderPassport> providerPassports() {
        Map<ProviderCode, ProviderPassport> passports = new LinkedHashMap<>();
        Set<String> displayNamesLower = new HashSet<>();

        for (MarketDataProvider provider : providers()) {
            ProviderCode code = provider.providerCode();
            ProviderPassport passport = provider.passport();

            // Проверка дублей по коду
            ProviderPassport prev = passports.put(code, passport);
            if (prev != null) {
                throw new ProviderCodeDuplicateException(code);
            }

            // Проверка дублей по отображаемому имени (без учёта регистра)
            String nameLower = passport.displayName().toLowerCase(Locale.ROOT);
            if (!displayNamesLower.add(nameLower)) {
                throw new ProviderDisplayNameDuplicateException(passport.displayName());
            }
        }
        return passports;
    }
}
