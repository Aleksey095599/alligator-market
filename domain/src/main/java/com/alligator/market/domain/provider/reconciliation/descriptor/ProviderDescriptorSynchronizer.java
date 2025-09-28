package com.alligator.market.domain.provider.reconciliation.descriptor;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.exception.ProviderDescriptorDuplicateException;
import com.alligator.market.domain.provider.reconciliation.ProviderContextScanner;
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
    public ProviderDescriptorSyncResult synchronize() {
        // 0) Считываем оба источника
        Map<String, ProviderDescriptor> contextDescriptors = contextScanner.providerDescriptors();
        Map<String, ProviderDescriptor> repositoryDescriptors = repository.findAll();

        int inContext = contextDescriptors.size();
        int inRepoBefore = repositoryDescriptors.size();

        // 1) Если в контексте пусто (нет дескрипторов) — очищаем репозиторий и выходим
        if (contextDescriptors.isEmpty()) {
            if (!repositoryDescriptors.isEmpty()) {
                repository.deleteAll();
                int deleted = repositoryDescriptors.size();
                return new ProviderDescriptorSyncResult(
                        inContext,
                        inRepoBefore,
                        deleted,
                        0,
                        0,
                        deleted > 0
                );
            }
            return new ProviderDescriptorSyncResult(
                    inContext,
                    inRepoBefore,
                    0,
                    0,
                    0,
                    false
            );
        }

        // 2) Проверяем инварианты уникальности наборов (код провайдера и имя провайдера)
        assertUniqueByCodeAndName(contextDescriptors);
        assertUniqueByCodeAndName(repositoryDescriptors);

        // Ранний выход: карты идентичны по ключам (кодам) и значениям (дескрипторам)
        if (repositoryDescriptors.equals(contextDescriptors)) {
            return new ProviderDescriptorSyncResult(
                    inContext,
                    inRepoBefore,
                    0,
                    0,
                    0,
                    false
            );
        }

        // 4.1) К удалению: дескрипторы в репозитории с кодами, которых больше нет в контексте
        Set<String> codesToDelete = new LinkedHashSet<>(repositoryDescriptors.keySet()); // Берем множество кодов из репозитория
        codesToDelete.removeAll(contextDescriptors.keySet()); // "Вычитаем" множество кодов из контекста

        // 4.2) К добавлению/обновлению: новые и изменившиеся дескрипторы
        Map<String, ProviderDescriptor> descriptorsToAdd = new LinkedHashMap<>(); // карта для новых дескрипторов
        Set<String> codesToUpdate = new LinkedHashSet<>(); // набор для кодов изменившихся дескрипторов
        // ↓↓ Перебираем элементы карты контекста
        for (Map.Entry<String, ProviderDescriptor> e : contextDescriptors.entrySet()) {
            String ctxProviderCode = e.getKey(); // Текущий индекс (он же код)
            ProviderDescriptor ctxDescriptor = e.getValue(); // Текущий элемент (он же дескриптор)
            // В карте репозитория ищем дескриптор с таким же кодом
            ProviderDescriptor maybeRepoDescriptor = repositoryDescriptors.get(ctxProviderCode);
            if (maybeRepoDescriptor == null) {
                descriptorsToAdd.put(ctxProviderCode, ctxDescriptor); // Значит ctxDescriptor — новый дескриптор
            } else if (!Objects.equals(maybeRepoDescriptor, ctxDescriptor)) {
                codesToUpdate.add(ctxProviderCode); // Значит ctxDescriptor изменился
            } // else — идентичен, ничего не делаем
        }

        // 5) Применяем изменения:
        //    Стратегия delete → insert: сначала удаляем из репозитория исчезнувшие и изменившиеся дескрипторы,
        //    затем выполняем batch INSERT для новых и изменившихся дескрипторов.
        //    Такой порядок прост и исключает конфликты уникальности.

        // 5.1) Собираем всё, что нужно удалить перед вставкой
        Set<String> codesToRemoveFirst = new LinkedHashSet<>(codesToDelete);
        codesToRemoveFirst.addAll(codesToUpdate);
        if (!codesToRemoveFirst.isEmpty()) {
            repository.deleteAllByProviderCodes(codesToRemoveFirst);
        }

        // 5.2) Формируем единый список для INSERT: новые + изменившиеся
        if (!descriptorsToAdd.isEmpty() || !codesToUpdate.isEmpty()) {
            Map<String, ProviderDescriptor> toInsert = new LinkedHashMap<>(descriptorsToAdd);
            for (String code : codesToUpdate) {
                toInsert.put(code, contextDescriptors.get(code));
            }
            // На крайний случай проверяем инварианты уникальности перед вставкой
            assertUniqueByCodeAndName(toInsert);
            repository.insertAll(toInsert); // выполняется пакетный INSERT
        }
        // Готово
        int deleted = codesToRemoveFirst.size();
        int insertedNew = descriptorsToAdd.size();
        int reinsertedUpdated = codesToUpdate.size();
        boolean changed = deleted > 0 || insertedNew > 0 || reinsertedUpdated > 0;
        return new ProviderDescriptorSyncResult(
                inContext,
                inRepoBefore,
                deleted,
                insertedNew,
                reinsertedUpdated,
                changed
        );
    }

    /* Проверка уникальности кодов провайдеров и отображаемых имен. */
    private static void assertUniqueByCodeAndName(Map<String, ProviderDescriptor> descriptors) {
        Set<String> seenProviderCodes = new HashSet<>();
        Set<String> seenDisplayNames = new HashSet<>();
        for (Map.Entry<String, ProviderDescriptor> entry : descriptors.entrySet()) {
            String code = entry.getKey();
            if (!seenProviderCodes.add(code)) {
                throw new ProviderDescriptorDuplicateException(code, entry.getValue().displayName());
            }
            ProviderDescriptor descriptor = entry.getValue();
            String displayName = descriptor.displayName();
            if (!seenDisplayNames.add(displayName)) {
                throw new ProviderDescriptorDuplicateException(code, displayName);
            }
        }
    }
}
