package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.backend.config.audit.ServiceAuditorContext;
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
    private final ServiceAuditorContext context;

    /** Сравнить профили и получить расхождения в виде {@link ContextDiff}. */
    public ContextDiff compare() {
        var domain = new ProviderProfilesReconciliation(contextScanner, profileStorage);
        return domain.compare();
    }

    /** Применить {@link ContextDiff} к хранилищу данных для синхронизации с контекстом приложения
     * информации о профилях. */
    public void applyContextDiffToStorage(ContextDiff diff) {
        var domain = new ProviderProfilesReconciliation(contextScanner, profileStorage);
        context.runWith("provider-sync-service", () -> domain.applyContextDiffToStorage(diff));
    }
}
