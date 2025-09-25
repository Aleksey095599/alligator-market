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

        // Если contextDescriptors пуст — очищаем репозиторий и выходим
        if (contextDescriptors.isEmpty()) {
            if (!repositoryDescriptors.isEmpty()) {
                repository.deleteAll();
            }
            return;
        }

        // Проверка уникальности contextDescriptors по коду провайдера и отображаемому имени
        assertUniqueByCodeAndName(contextDescriptors);
        // Строим карту репозитория: <код провайдера, дескриптор в репозитории>
        Map<String, ProviderDescriptor> repoMap = toMapByCode(repositoryDescriptors);

        // Проверка уникальности repositoryDescriptors по коду провайдера и отображаемому имени
        assertUniqueByCodeAndName(repositoryDescriptors);
        // Строим карту контекста: <код провайдера, дескриптор в контексте>
        Map<String, ProviderDescriptor> ctxMap = toMapByCode(contextDescriptors);

        // Ранний выход: карты идентичны по ключам и значениям
        if (repoMap.equals(ctxMap)) {
            return;
        }

        // Берем множество кодов провайдеров в repoMap...
        Set<String> codesToDelete = new LinkedHashSet<>(repoMap.keySet());
        // ... вычитаем из него множество кодов провайдеров в ctxMap — получаем коды к удалению из репозитория
        codesToDelete.removeAll(ctxMap.keySet());

        // На upsert: новые или изменившиеся дескрипторы
        List<ProviderDescriptor> descriptorsToUpsert = new ArrayList<>();
        for (Map.Entry<String, ProviderDescriptor> e : ctxMap.entrySet()) { // Перебираем элементы карты ctxMap
            // ↓↓ Текущие индекс и элемент карты ctxMap
            String ctxProviderCode = e.getKey();
            ProviderDescriptor ctxDescriptor = e.getValue();
            // В карте repoMap ищем дескриптор с таким же кодом провайдера
            ProviderDescriptor maybeRepoDescriptor = repoMap.get(ctxProviderCode);
            // Если не найден или найден, но не совпадает — значит новый или изменившийся
            if (maybeRepoDescriptor == null || !maybeRepoDescriptor.equals(ctxDescriptor)) {
                descriptorsToUpsert.add(ctxDescriptor);
            }
        }

        // Применяем изменения
        if (!codesToDelete.isEmpty()) repository.deleteAllByProviderCodes(codesToDelete);
        if (!descriptorsToUpsert.isEmpty()) repository.saveAll(descriptorsToUpsert);
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
