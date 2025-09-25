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

        // Проверка уникальности дескрипторов из контекста по коду провайдера и отображаемому имени
        assertUniqueByCodeAndName(contextDescriptors);
        // Строим карту
        Map<String, ProviderDescriptor> repoMap = toMapByCode(repositoryDescriptors);

        // Проверка уникальности дескрипторов из репозитория по коду провайдера и отображаемому имени
        assertUniqueByCodeAndName(repositoryDescriptors);
        // Строим карту
        Map<String, ProviderDescriptor> ctxMap  = toMapByCode(contextDescriptors);

        // Ранний выход: карты идентичны по ключам и значениям
        if (repoMap.equals(ctxMap)) {
            return;
        }


        // Комментарий:
        // Репозиторий: D1:{providerCode1; ...}, D2:{providerCode2; ...}, D3:{providerCode3; ...}
        // Контекст:


        // Если в repoMap есть такие индексы, которых нет в ctxMap, — связанные с ними дескрипторы нужно удалить
        Set<String> codesToDelete = new LinkedHashSet<>(repoMap.keySet());
        codesToDelete.removeAll(ctxMap.keySet());

        // На upsert: новые или изменившиеся дескрипторы
        List<ProviderDescriptor> descriptorsToUpsert = new ArrayList<>();
        for (Map.Entry<String, ProviderDescriptor> e : ctxMap.entrySet()) { // Перебираем элементы карты контекста
            // Текущий индекс элемента
            String currentIndex = e.getKey();
            // Текущий дескриптор элемента
            ProviderDescriptor CurrentCtxDescriptor = e.getValue();
            // Получаем дескриптор из карты репозитория с таким же индексом (если он есть)
            ProviderDescriptor maybeRepoDescriptor = repoMap.get(currentIndex);
            if (maybeRepoDescriptor == null // Значит это новый дескриптор
                    || !maybeRepoDescriptor.equals(CurrentCtxDescriptor) // Значит дескриптор изменился
            ) {
                descriptorsToUpsert.add(CurrentCtxDescriptor);
            }
        }

        // Для страховки проверим, что descriptorsToUpsert уникальны по коду провайдера и отображаемому имени
        assertUniqueByCodeAndName(descriptorsToUpsert);

        // Применяем изменения
        if (!codesToDelete.isEmpty()) repository.deleteAllByProviderCodes(codesToDelete);
        if (!descriptorsToUpsert.isEmpty()) repository.saveAll(descriptorsToUpsert);
    }

    /* Нормализуем код провайдера: убираем пробелы и приводим к верхнему регистру. */
    private static String norm(String code) {
        return code == null ? null : code.trim().toUpperCase(Locale.ROOT);
    }

    /* Проверка уникальности кодов провайдеров и отображаемых имен. */
    private static void assertUniqueByCodeAndName(List<ProviderDescriptor> descriptors) {
        Set<String> seenCodes = new HashSet<>();
        Set<String> seenDisplayNames = new HashSet<>();
        for (ProviderDescriptor d : descriptors) {
            String code = norm(d.providerCode());
            if (!seenCodes.add(code)) {
                throw new ProviderDescriptorDuplicateException(code, d.displayName());
            }

            String displayName = normDisplayName(d.displayName());
            if (!seenDisplayNames.add(displayName)) {
                throw new ProviderDescriptorDuplicateException(d.providerCode(), d.displayName());
            }
        }
    }

    /* Нормализуем отображаемое имя провайдера: убираем пробелы. */
    private static String normDisplayName(String displayName) {
        return displayName == null ? null : displayName.trim();
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
