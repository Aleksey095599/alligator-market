package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.reppository.ProviderDescriptorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Сервис синхронизации дескрипторов провайдеров рыночных данных.
 */
@Service
public class ProviderDescriptorSynchronizer {

    private static final Logger log = LoggerFactory.getLogger(ProviderDescriptorSynchronizer.class);

    /* ↓↓ Сканер контекста приложения: предоставляет актуальные дескрипторы провайдеров. */
    private final ProviderContextScanner contextScanner;

    /* ↓↓ Репозиторий дескрипторов провайдеров. */
    private final ProviderDescriptorRepository repository;

    public ProviderDescriptorSynchronizer(
            ProviderContextScanner contextScanner,
            ProviderDescriptorRepository repository
    ) {
        this.contextScanner = contextScanner;
        this.repository = repository;
    }

    /** Выполнить синхронизацию дескрипторов провайдеров. */
    public void synchronize() {
        var descriptors = contextScanner.providerDescriptors(); // Загружаем дескрипторы из контекста

        // ↓↓ Устраняем дубликаты по providerCode, сохраняя порядок регистрации.
        var deduplicated = new LinkedHashMap<String, ProviderDescriptor>();
        var duplicates = 0;
        for (var descriptor : descriptors) {
            var previous = deduplicated.put(descriptor.providerCode(), descriptor);
            if (previous != null) {
                duplicates++;
            }
        }
        if (duplicates > 0) {
            log.warn("Duplicate provider descriptors detected: {} duplicates removed", duplicates);
        }

        var uniqueDescriptors = new ArrayList<>(deduplicated.values());

        repository.deleteAll(); // Полностью очищаем репозиторий
        repository.saveAll(uniqueDescriptors); // Сохраняем обновлённый набор дескрипторов
    }
}
