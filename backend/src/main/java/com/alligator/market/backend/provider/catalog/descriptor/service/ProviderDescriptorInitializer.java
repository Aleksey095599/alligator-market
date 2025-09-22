package com.alligator.market.backend.provider.catalog.descriptor.service;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.reconciliation.ProviderContextScanner;
import com.alligator.market.domain.provider.reppository.ProviderDescriptorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Компонент, обновляющий каталог дескрипторов провайдеров при старте приложения.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProviderDescriptorInitializer implements ApplicationRunner {

    private final ProviderContextScanner scanner;
    private final ProviderDescriptorRepository repository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        // Получаем актуальный список дескрипторов провайдеров из контекста
        List<ProviderDescriptor> descriptors = scanner.providerDescriptors();

        // Очищаем таблицу и пересохраняем найденные дескрипторы
        repository.deleteAll();
        repository.saveAll(descriptors);

        log.info("Provider descriptors refreshed: {} item(s)", descriptors.size());
    }
}
