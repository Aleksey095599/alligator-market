package com.alligator.market.domain.provider.reconciliation.dto;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;

import java.util.Locale;
import java.util.Objects;

/**
 * Снимок провайдера для процесса синхронизации.
 */
public record ProviderSnapshot(
        String code,
        ProviderDescriptor descriptor,
        ProviderPolicy policy
) {
    /**
     * Конструктор с проверками и нормализацией.
     */
    public ProviderSnapshot {
        Objects.requireNonNull(code, "Provider code must not be null");
        Objects.requireNonNull(descriptor, "Provider descriptor must not be null");
        Objects.requireNonNull(policy, "Provider policy must not be null");

        // Нормализуем код провайдера
        String c = code.strip(); // Обрезаем пробелы
        if (c.isEmpty()) throw new IllegalArgumentException("Provider code must not be blank");
        // Проверяем ограничение домена: максимум 50 символов
        if (c.length() > 50)
            throw new IllegalArgumentException("Provider code length must be less or equal to 50 characters");
        c = c.toUpperCase(Locale.ROOT);

        // Присваиваем проверенное и нормализованное значение
        code = c;
    }
}
