package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.reppository.ProviderDescriptorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Доменный сервис синхронизации дескрипторов провайдеров рыночных данных.
 */
@Service
public class ProviderDescriptorSynchronizer {

    private static final Logger log = LoggerFactory.getLogger(ProviderDescriptorSynchronizer.class);

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

        // ↓↓ Проверяем отсутствие дубликатов по providerCode, сохраняя порядок регистрации.
        var deduplicated = new LinkedHashMap<String, ProviderDescriptor>();
        for (var descriptor : descriptors) {
            var providerCode = descriptor.providerCode();
            if (deduplicated.containsKey(providerCode)) {
                var message = "Duplicate provider descriptor detected for provider code: " + providerCode;
                log.error(message);
                throw new IllegalStateException(message);
            }
            deduplicated.put(providerCode, descriptor);
        }

        var uniqueDescriptors = new ArrayList<>(deduplicated.values());

        repository.deleteAll(); // Полностью очищаем репозиторий
        repository.saveAll(uniqueDescriptors); // Сохраняем обновлённый набор дескрипторов
    }
}
