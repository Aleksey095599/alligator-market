package com.alligator.market.domain.provider.reconciliation.descriptor;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
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

    /**
     * Выполнить синхронизацию.
     */
    public void synchronize() {

        // 1) Считываем оба источника и получаем карты <код провайдера, дескриптор>
        Map<String, ProviderDescriptor> ctxMap = contextScanner.providerDescriptors();
        Map<String, ProviderDescriptor> repoMap = repository.findAll();

        // 2) Если в контексте нет дескрипторов — очищаем репозиторий и выходим
        if (ctxMap.isEmpty()) {
            if (!repoMap.isEmpty()) {
                repository.deleteAll();
            }
            return;
        }

        // 3) Проверяем уникальности кода и имени провайдера в обеих картах
        assertUniqueByCodeAndName(ctxMap);
        assertUniqueByCodeAndName(repoMap);

        // Ранний выход: карты идентичны по ключам (кодам провайдеров) и значениям (дескрипторам)
        if (repoMap.equals(ctxMap)) {
            return;
        }

        // 4.1) К удалению: дескрипторы в репозитории с "устаревшими" кодами провайдеров
        Set<String> codesToDelete = new LinkedHashSet<>(repoMap.keySet()); // Берем все коды провайдеров из repoMap
        codesToDelete.removeAll(ctxMap.keySet()); // Удаляем из них те коды, которые встречаются ctxMap

        // 4.2) К добавлению/обновлению: новые и изменившиеся дескрипторы
        Map<String, ProviderDescriptor> descriptorsToAdd = new LinkedHashMap<>(); // карта для новых дескрипторов
        Set<String> codesToUpdate = new LinkedHashSet<>(); // набор для кодов изменившихся дескрипторов
        // ↓↓ Перебираем элементы карты ctxMap
        for (Map.Entry<String, ProviderDescriptor> e : ctxMap.entrySet()) {
            String ctxProviderCode = e.getKey(); // Текущий индекс (он же код провайдера)
            ProviderDescriptor ctxDescriptor = e.getValue(); // Текущий элемент (он же дескриптор)
            // В карте repoMap ищем дескриптор с таким же кодом
            ProviderDescriptor maybeRepoDescriptor = repoMap.get(ctxProviderCode);
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
        Set<String> codesToRemoveFirst = new LinkedHashSet<>(codesToDelete); // Берем коды к удалению
        codesToRemoveFirst.addAll(codesToUpdate); // Добавляем коды изменившихся дескрипторов
        if (!codesToRemoveFirst.isEmpty()) {
            repository.deleteAllByProviderCodes(codesToRemoveFirst);
        }

        // 5.2) Формируем единый список для INSERT: новые + изменившиеся
        if (!descriptorsToAdd.isEmpty() || !codesToUpdate.isEmpty()) {
            Map<String, ProviderDescriptor> toInsert = new LinkedHashMap<>(descriptorsToAdd);
            for (String code : codesToUpdate) {
                toInsert.put(code, ctxMap.get(code));
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
}
