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
        // 0) Считываем оба источника
        List<ProviderDescriptor> contextDescriptors = contextScanner.providerDescriptors();
        List<ProviderDescriptor> repositoryDescriptors = repository.findAll();

        // 1) Если в контексте пусто (нет дескрипторов) — очищаем репозиторий и выходим
        if (contextDescriptors.isEmpty()) {
            if (!repositoryDescriptors.isEmpty()) {
                repository.deleteAll();
            }
            return;
        }

        // 2) Проверяем инварианты уникальности наборов (код провайдера и имя провайдера)
        assertUniqueByCodeAndName(contextDescriptors);
        assertUniqueByCodeAndName(repositoryDescriptors);

        // 3) Строим карты по коду провайдера (далее для краткости "код провайдера" = "код")
        Map<String, ProviderDescriptor> repoMap = toMapByCode(repositoryDescriptors);
        Map<String, ProviderDescriptor> ctxMap  = toMapByCode(contextDescriptors);

        // Ранний выход: карты идентичны по ключам (кодам) и значениям (дескрипторам)
        if (repoMap.equals(ctxMap)) {
            return;
        }

        // 4.1) К удалению: дескрипторы в репозитории с кодами, которых больше нет в контексте
        Set<String> codesToDelete = new LinkedHashSet<>(repoMap.keySet()); // Берем множество кодов из repoMap
        codesToDelete.removeAll(ctxMap.keySet()); // "Вычитаем" множество кодов из ctxMap

        // 4.2) К добавлению/обновлению: новые и изменившиеся дескрипторы
        List<ProviderDescriptor> descriptorsToAdd = new ArrayList<>(); // список для новых дескрипторов
        Set<String> codesToUpdate = new LinkedHashSet<>(); // набор для кодов изменившиеся дескрипторов
        // ↓↓ Перебираем элементы карты ctxMap
        for (Map.Entry<String, ProviderDescriptor> e : ctxMap.entrySet()) {
            String ctxProviderCode = e.getKey(); // Текущий индекс (он же код)
            ProviderDescriptor ctxDescriptor = e.getValue(); // Текущий элемент (он же дескриптор)
            // В карте repoMap ищем дескриптор с таким же кодом
            ProviderDescriptor maybeRepoDescriptor = repoMap.get(ctxProviderCode);
            if (maybeRepoDescriptor == null) {
                descriptorsToAdd.add(ctxDescriptor); // Значит ctxDescriptor — новый дескриптор
            } else if (maybeRepoDescriptor != ctxDescriptor) {
                codesToUpdate.add(ctxProviderCode); // Значит ctxDescriptor изменился
            } // else — идентичен, ничего не делаем
        }

        // 5) Применяем изменения атомарно (одна транзакция на весь блок).
        //    Стратегия: сначала удаляем из репозитория исчезнувшие и изменившиеся дескрипторы,
        //    затем выполняем batch UPSERT для новых и изменившихся дескрипторов.
        //    Такой порядок прост и исключает конфликты уникальности.

        // 5.1) Собираем всё, что нужно удалить перед вставкой
        Set<String> codesToRemoveFirst = new LinkedHashSet<>(codesToDelete);
        codesToRemoveFirst.addAll(codesToUpdate);
        if (!codesToRemoveFirst.isEmpty()) {
            repository.deleteAllByProviderCodes(codesToRemoveFirst);
        }

        // 5.2) Формируем единый список для UPSERT: новые + изменившиеся
        if (!descriptorsToAdd.isEmpty() || !codesToUpdate.isEmpty()) {
            List<ProviderDescriptor> toUpsert = new ArrayList<>(descriptorsToAdd);
            for (String code : codesToUpdate) {
                toUpsert.add(ctxMap.get(code));
            }
            repository.saveAll(toUpsert); // семантика UPSERT по providerCode
        }
        // Готово
    }

    /* Проверка уникальности кодов провайдеров и отображаемых имен. */
    private static void assertUniqueByCodeAndName(List<ProviderDescriptor> descriptors) {
        Set<String> seenProviderCodes = new HashSet<>();
        Set<String> seenDisplayNames = new HashSet<>();
        for (ProviderDescriptor d : descriptors) {
            String code = d.providerCode();
            if (!seenProviderCodes.add(code)) {
                throw new ProviderDescriptorDuplicateException(d.providerCode(), d.displayName());
            }
            String displayName = d.displayName();
            if (!seenDisplayNames.add(displayName)) {
                throw new ProviderDescriptorDuplicateException(d.providerCode(), d.displayName());
            }
        }
    }

    /* Построить карту: providerCode ⇒ ProviderDescriptor. */
    private static Map<String, ProviderDescriptor> toMapByCode(List<ProviderDescriptor> list) {
        Map<String, ProviderDescriptor> map = new LinkedHashMap<>(); // сохраняем порядок для предсказуемых логов
        for (ProviderDescriptor d : list) {
            map.put(d.providerCode(), d);
        }
        return map;
    }
}
