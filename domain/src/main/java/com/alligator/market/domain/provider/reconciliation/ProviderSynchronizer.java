package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.reconciliation.scanner.ProviderContextScanner;
import com.alligator.market.domain.provider.repository.ProviderDescriptorRepository;

import java.util.*;

/**
 * Доменный сервис синхронизации провайдеров в контексте приложения и в репозитории.
 * TODO: требуется расширить логику не только для дескрипторов но и с учётом всей сущности ProviderEntity.
 */
public class ProviderSynchronizer {

    /* Сканер, извлекающий дескрипторы из контекста приложения. */
    private final ProviderContextScanner contextScanner;

    /* Репозиторий дескрипторов провайдеров. */
    private final ProviderDescriptorRepository repository;

    /** Конструктор. */
    public ProviderSynchronizer(
            ProviderContextScanner contextScanner,
            ProviderDescriptorRepository repository
    ) {
        this.contextScanner = contextScanner;
        this.repository = repository;
    }

    /** Выполнить синхронизацию. */
    public void synchronize() {

        // 1) Считываем оба источника и получаем карты <код провайдера, дескриптор>
        Map<String, ProviderDescriptor> ctxMap = contextScanner.providerDescriptors();
        Map<String, ProviderDescriptor> repoMap = repository.findAll();

        // Проверки на уникальность кодов и имен провайдеров не нужны:
        // для ctxMap это гарантирует сканер, для repoMap — структура таблицы БД.

        // 2) Если в контексте нет дескрипторов — очищаем репозиторий и выходим
        if (ctxMap.isEmpty()) {
            if (!repoMap.isEmpty()) {
                repository.deleteAll();
            }
            return;
        }

        // Ранний выход, если карты идентичны
        if (repoMap.equals(ctxMap)) {
            return;
        }

        // 3.1) Дескрипторы в репозитории с "устаревшими" кодами провайдеров
        Set<String> obsoleteCodes = new LinkedHashSet<>(repoMap.keySet()); // Берем все коды провайдеров из repoMap
        obsoleteCodes.removeAll(ctxMap.keySet()); // Удаляем из них те коды, которые встречаются ctxMap

        // 3.2) Новые или изменившиеся дескрипторы
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

        // Далее применяем изменения:
        // Стратегия delete → insert: сначала удаляем из репозитория исчезнувшие и изменившиеся дескрипторы,
        // затем выполняем batch INSERT для новых и изменившихся дескрипторов.
        // Такой порядок прост и исключает конфликты уникальности.

        // 4.1) Собираем всё, что нужно удалить перед вставкой
        Set<String> codesToDelete = new LinkedHashSet<>(obsoleteCodes); // Берем "устаревшие" коды
        codesToDelete.addAll(codesToUpdate); // Добавляем коды изменившихся дескрипторов
        if (!codesToDelete.isEmpty()) {
            repository.deleteAllByProviderCodes(codesToDelete); // Производим удаление дескрипторов по кодам
        }

        // 4.2) Формируем единый список для INSERT: новые + изменившиеся
        if (!descriptorsToAdd.isEmpty() || !codesToUpdate.isEmpty()) {
            Map<String, ProviderDescriptor> toInsert = new LinkedHashMap<>(descriptorsToAdd);
            for (String code : codesToUpdate) {
                toInsert.put(code, ctxMap.get(code));
            }
            repository.insertAll(toInsert); // выполняется пакетный INSERT
        }
        // Готово
    }
}
