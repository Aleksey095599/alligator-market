package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.exception.ProviderDescriptorDuplicateException;
import com.alligator.market.domain.provider.repository.ProviderDescriptorRepository;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Доменный сервис синхронизации дескрипторов провайдеров в контексте приложения и в репозитории.
 */
public class ProviderDescriptorSynchronizer {

    /* Сканер, извлекающий дескрипторы из контекста приложения. */
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
        var contextDescriptors = contextScanner.providerDescriptors();

        // Проверяем, что список дескрипторов не пуст
        if (contextDescriptors.isEmpty()) {
            throw new IllegalStateException("No provider descriptors found");
        }

        // Проверяем отсутствие дубликатов по providerCode
        var uniqueDescriptors = deduplicateDescriptors(contextDescriptors);

        // Загружаем сохранённые ранее дескрипторы
        var repositoryDescriptors = repository.findAll();

        // Оптимизация: если наборы совпадают, то повторное сохранение не требуется
        if (repositoryDescriptors.size() == uniqueDescriptors.size()
                && repositoryDescriptors.containsAll(uniqueDescriptors)
                && uniqueDescriptors.containsAll(repositoryDescriptors)) {
            return;
        }

        // Собираем карту дескрипторов из репозитория по коду провайдера для быстрых проверок
        var repositoryMap = new LinkedHashMap<String, ProviderDescriptor>();
        for (var descriptor : repositoryDescriptors) {
            repositoryMap.put(descriptor.providerCode(), descriptor);
        }

        // Формируем списки на удаление и добавление
        var descriptorsToSave = new ArrayList<ProviderDescriptor>();
        Set<String> codesToDelete = new LinkedHashSet<>();

        for (var descriptor : uniqueDescriptors) {
            var providerCode = descriptor.providerCode();
            var existing = repositoryMap.get(providerCode);

            if (existing == null) { // Новый код
                descriptorsToSave.add(descriptor);
                continue;
            }

            if (!existing.equals(descriptor)) { // Код есть, но данные изменились
                descriptorsToSave.add(descriptor);
                codesToDelete.add(providerCode);
            }
        }

        // Вычисляем коды, которые присутствуют в репозитории, но отсутствуют в контексте
        var contextCodes = new LinkedHashSet<String>();
        for (var descriptor : uniqueDescriptors) {
            contextCodes.add(descriptor.providerCode());
        }
        for (var descriptor : repositoryDescriptors) {
            var providerCode = descriptor.providerCode();
            if (!contextCodes.contains(providerCode)) {
                codesToDelete.add(providerCode);
            }
        }

        if (!codesToDelete.isEmpty()) {
            repository.deleteAllByProviderCodes(codesToDelete); // Удаляем изменившиеся и устаревшие дескрипторы
        }

        if (!descriptorsToSave.isEmpty()) {
            repository.saveAll(descriptorsToSave); // Добавляем новые или обновлённые дескрипторы
        }
    }

    /** Статический метод проверки дескрипторов на дублирование по кодам провайдеров. */
    private static ArrayList<ProviderDescriptor> deduplicateDescriptors(List<ProviderDescriptor> descriptors) {
        var deduplicated = new LinkedHashMap<String, ProviderDescriptor>();
        for (var descriptor : descriptors) {
            var providerCode = descriptor.providerCode();
            if (deduplicated.containsKey(providerCode)) {
                throw new ProviderDescriptorDuplicateException(providerCode);
            }
            deduplicated.put(providerCode, descriptor);
        }
        return new ArrayList<>(deduplicated.values());
    }
}
