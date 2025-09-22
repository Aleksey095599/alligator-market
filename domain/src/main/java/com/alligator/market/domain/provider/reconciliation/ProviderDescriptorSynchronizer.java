package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.reppository.ProviderDescriptorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Доменный сервис синхронизации дескрипторов провайдеров рыночных данных.
 */
@Service
public class ProviderDescriptorSynchronizer {

    /* Сканер контекста приложения: предоставляет актуальные дескрипторы провайдеров. */
    private final ProviderContextScanner contextScanner;

    /* Репозиторий дескрипторов провайдеров. */
    private final ProviderDescriptorRepository repository;

    /** Конструктор. */
    public ProviderDescriptorSynchronizer(
            ProviderContextScanner contextScanner,
            ProviderDescriptorRepository repository
    ) {
        this.contextScanner = contextScanner;
        this.repository = repository;
    }

    /** Выполнить синхронизацию дескрипторов провайдеров. */
    public void synchronize() {
        // Загружаем дескрипторы из контекста
        var descriptors = contextScanner.providerDescriptors();

        // Проверяем, что список не пуст
        if (descriptors.isEmpty()) {
            throw new IllegalStateException("No provider descriptors found");
        }

        // Проверяем отсутствие дубликатов по providerCode
        var uniqueDescriptors = deduplicateDescriptors(descriptors);

        repository.deleteAll();                // Полностью очищаем репозиторий
        repository.saveAll(uniqueDescriptors); // Сохраняем обновлённый набор дескрипторов
    }

    /** Статический метод проверки дескрипторов на дублирование по кодам провайдеров. */
    private static ArrayList<ProviderDescriptor> deduplicateDescriptors(List<ProviderDescriptor> descriptors) {
        var deduplicated = new LinkedHashMap<String, ProviderDescriptor>();
        for (var descriptor : descriptors) {
            var providerCode = descriptor.providerCode();
            if (deduplicated.containsKey(providerCode)) {
                var message = "Duplicate provider descriptor detected for provider code: " + providerCode;
                throw new IllegalStateException(message);
            }
            deduplicated.put(providerCode, descriptor);
        }
        return new ArrayList<>(deduplicated.values());
    }
}
