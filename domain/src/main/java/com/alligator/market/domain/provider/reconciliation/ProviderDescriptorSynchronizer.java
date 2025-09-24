package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.exception.ProviderDescriptorDuplicateException;
import com.alligator.market.domain.provider.repository.ProviderDescriptorRepository;

import java.util.*;

/**
 * Доменный сервис синхронизации дескрипторов провайдеров в контексте приложения и в репозитории.
 */
public class ProviderDescriptorSynchronizer {

    /* Сканер, извлекающий дескрипторы из контекста приложения. */
    private final ProviderContextScanner contextScanner;

    /* Репозиторий дескрипторов провайдеров. */
    private final ProviderDescriptorRepository repository;

    /* Конструктор. */
    public ProviderDescriptorSynchronizer(
            ProviderContextScanner contextScanner,
            ProviderDescriptorRepository repository
    ) {
        this.contextScanner = contextScanner;
        this.repository = repository;
    }

    /** Выполнить синхронизацию дескрипторов провайдеров. */
    public void synchronize() {
        // Список дескрипторов из контекста
        List<ProviderDescriptor> contextDescriptors = contextScanner.providerDescriptors();
        // Список дескрипторов из репозитория
        List<ProviderDescriptor> repositoryDescriptors = repository.findAll();

        // Если контекст пуст — очищаем репозиторий и выходим
        if (contextDescriptors.isEmpty()) {
            if (!repositoryDescriptors.isEmpty()) {
                repository.deleteAll();
            }
            return;
        }

        // ↓↓ Проверяем отсутствие дубликатов по коду провайдера
        assertUniqueByCode(contextDescriptors);
        assertUniqueByCode(repositoryDescriptors);

        // ↓↓ Индексируем списки по коду провайдера (индекс = код провайдера)
        Map<String, ProviderDescriptor> repoMap = toMapByCode(repositoryDescriptors);
        Map<String, ProviderDescriptor> ctxMap  = toMapByCode(contextDescriptors);

        // Ранний выход: снимки идентичны по ключам и значениям
        if (repoMap.equals(ctxMap)) {
            return;
        }

        // Если в repoMap есть такие индексы, которых нет в ctxMap, — связанные с ними дескрипторы нужно удалить
        Set<String> codesToDelete = new LinkedHashSet<>(repoMap.keySet());
        codesToDelete.removeAll(ctxMap.keySet());

        // На upsert: новые или изменившиеся дескрипторы
        List<ProviderDescriptor> descriptorsToUpsert = new ArrayList<>();
        for (Map.Entry<String, ProviderDescriptor> e : ctxMap.entrySet()) { // Перебираем карту контекста
            // Текущий индекс из карты контекста
            String currentIndex = e.getKey();
            // Текущий дескриптор из карты контекста
            ProviderDescriptor CurrentCtxDescriptor = e.getValue();
            // Получаем дескриптор из карты репозитория с таким же индексом
            ProviderDescriptor maybeRepoDescriptor = repoMap.get(currentIndex);
            if (maybeRepoDescriptor == null // Значит это новый дескриптор в контексте
                    || !maybeRepoDescriptor.equals(CurrentCtxDescriptor) // Значит дескриптор обновился
            ) {
                descriptorsToUpsert.add(CurrentCtxDescriptor);
            }
        }

        // Применяем изменения
        if (!codesToDelete.isEmpty()) repository.deleteAllByProviderCodes(codesToDelete);
        if (!descriptorsToUpsert.isEmpty()) repository.saveAll(descriptorsToUpsert);
    }

    /* Нормализуем код провайдера: убираем пробелы и приводим к верхнему регистру. */
    private static String norm(String code) {
        return code == null ? null : code.trim().toUpperCase(Locale.ROOT);
    }

    /* Проверка уникальности кодов провайдеров. */
    private static void assertUniqueByCode(List<ProviderDescriptor> descriptors) {
        Set<String> seen = new HashSet<>();
        for (ProviderDescriptor d : descriptors) {
            String code = norm(d.providerCode());
            if (!seen.add(code)) {
                throw new ProviderDescriptorDuplicateException(code);
            }
        }
    }

    /* Построить карту: providerCode ⇒ descriptor. */
    private static Map<String, ProviderDescriptor> toMapByCode(List<ProviderDescriptor> list) {
        Map<String, ProviderDescriptor> map = new LinkedHashMap<>(); // сохраняем порядок для предсказуемых логов
        for (ProviderDescriptor d : list) {
            map.put(norm(d.providerCode()), d);
        }
        return map;
    }
}
