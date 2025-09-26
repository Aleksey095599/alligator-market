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

        // 1) Если в контексте пусто — очищаем репозиторий и выходим
        if (contextDescriptors.isEmpty()) {
            if (!repositoryDescriptors.isEmpty()) {
                repository.deleteAll();
            }
            return;
        }

        // 2) Проверяем инварианты уникальности наборов (код провайдера и имя провайдера)
        assertUniqueByCodeAndName(contextDescriptors);
        assertUniqueByCodeAndName(repositoryDescriptors);

        // 3) Строим карты по коду провайдера (далее просто код)
        Map<String, ProviderDescriptor> repoMap = toMapByCode(repositoryDescriptors);
        Map<String, ProviderDescriptor> ctxMap  = toMapByCode(contextDescriptors);

        // Ранний выход: карты идентичны по ключам (кодам) и значениям (дескрипторам)
        if (repoMap.equals(ctxMap)) {
            return;
        }

        // 4.1) К удалению — дескрипторы в репозитории с кодами, которых больше нет в контексте
        Set<String> codesToDelete = new LinkedHashSet<>(repoMap.keySet()); // Берем множество кодов в repoMap
        codesToDelete.removeAll(ctxMap.keySet()); // "Вычитаем" из него множество кодов в ctxMap





        // ↓↓ Список для новых дескрипторов и набор для кодов дескрипторов к обновлению
        List<ProviderDescriptor> descriptorsToAdd = new ArrayList<>();
        Set<String> codesToUpdate = new LinkedHashSet<>();

        for (Map.Entry<String, ProviderDescriptor> e : ctxMap.entrySet()) { // Перебираем элементы карты ctxMap
            // ↓↓ Текущие индекс (он же код провайдера) и элемент карты ctxMap
            String ctxProviderCode = e.getKey();
            ProviderDescriptor ctxDescriptor = e.getValue();
            // В карте repoMap ищем дескриптор с таким же кодом провайдера
            ProviderDescriptor maybeRepoDescriptor = repoMap.get(ctxProviderCode);
            if (maybeRepoDescriptor == null) { // Значит ctxDescriptor новый дескриптор
                descriptorsToAdd.add(ctxDescriptor);
            } else {
                if (!maybeRepoDescriptor.equals(ctxDescriptor) { // Значит ctxDescriptor нужно обновить
                    codesToUpdate.add(ctxProviderCode);
                } else {} // ничего делать не нужно — ctxDescriptor уже есть в репозитории
            }
        }

        // Применяем изменения
        if (!codesToDelete.isEmpty()) repository.deleteAllByProviderCodes(codesToDelete);
        if (!codesToUpdate) repository.
        if (!descriptorsToAdd.isEmpty()) repository.
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
