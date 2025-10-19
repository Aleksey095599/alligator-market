package com.alligator.market.domain.provider.reconciliation.dto;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;

import java.util.Locale;
import java.util.Objects;

/** Снимок провайдера для процесса синхронизации. */
public record ProviderSnapshot(
        String code,
        ProviderDescriptor descriptor,
        ProviderPolicy policy
) {
    /** Конструктор с проверками и нормализацией. */
    public ProviderSnapshot {
        // ↓↓ fail-fast проверки
        Objects.requireNonNull(code, "code must not be null");
        Objects.requireNonNull(descriptor, "descriptor must not be null");
        Objects.requireNonNull(policy, "policy must not be null");

        String c = code.strip(); // Обрезаем пробелы
        if (c.isEmpty()) throw new IllegalArgumentException("code must not be blank");
        // Нормализуем код до UPPER CASE (БД требует upper(provider_code), см. CHECK)
        c = c.toUpperCase(Locale.ROOT);

        // Присваиваем нормализованное/проверенное значение в поле code
        code = c;
    }
}
