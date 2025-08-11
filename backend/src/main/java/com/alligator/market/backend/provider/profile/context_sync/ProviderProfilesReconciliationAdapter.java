package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.domain.provider.context_sync.ContextDiff;
import com.alligator.market.domain.provider.context_sync.ProviderContextScanner;
import com.alligator.market.domain.provider.context_sync.ProviderProfilesReconciliation;
import com.alligator.market.domain.provider.profile.catalog.ProviderProfileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Компонент вызывает доменную логику сопоставления профилей провайдеров рыночных данных (далее - профили),
 * извлеченных из контекста приложения и хранилища данных.
 */
@Component
@RequiredArgsConstructor
public class ProviderProfilesReconciliationAdapter {

    private final ProviderContextScanner contextScanner;
    private final ProviderProfileStorage profileStorage;

    /** Сравнить профили и получить расхождения в виде {@link ContextDiff}. */
    public ContextDiff compare() {
        var domainReconciliationService = new ProviderProfilesReconciliation(contextScanner, profileStorage);
        return domainReconciliationService.compare();
    }

    /** Применить {@link ContextDiff} к хранилищу данных, выполняя задачу от имени системного пользователя. */
    public void applyContextDiffToStorage(ContextDiff diff) {
        var domainReconciliationService = new ProviderProfilesReconciliation(contextScanner, profileStorage);
        domainReconciliationService.applyContextDiffToStorage(diff);
    }
}
