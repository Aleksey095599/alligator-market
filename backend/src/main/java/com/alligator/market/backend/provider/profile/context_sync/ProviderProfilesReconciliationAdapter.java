package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.backend.config.audit.AuditContext;
import com.alligator.market.backend.config.audit.AuditContextHolder;
import com.alligator.market.domain.provider.context_sync.ContextDiff;
import com.alligator.market.domain.provider.context_sync.ProviderContextScanner;
import com.alligator.market.domain.provider.context_sync.ProviderProfilesReconciliation;
import com.alligator.market.domain.provider.profile.catalog.ProviderProfileStorage;
import org.springframework.stereotype.Component;

/**
 * Компонент вызывает доменную логику сопоставления профилей провайдеров рыночных данных (далее - профили),
 * извлеченных из контекста приложения и хранилища данных.
 */
@Component
public class ProviderProfilesReconciliationAdapter {

    /** Внутренний источник операции. */
    private static final String VIA = "provider-profiles-reconciliation";

    /** Доменная логика сопоставления профилей. */
    private final ProviderProfilesReconciliation reconciliation;

    // Конструктор
    public ProviderProfilesReconciliationAdapter(
            ProviderContextScanner contextScanner,
            ProviderProfileStorage profileStorage
    ) {
        this.reconciliation = new ProviderProfilesReconciliation(contextScanner, profileStorage);
    }

    /** Сравнить профили и получить расхождения в виде {@link ContextDiff}. */
    public ContextDiff compare() {
        return reconciliation.compare();
    }

    /** Применить {@link ContextDiff} к хранилищу данных, выполняя задачу от имени системного пользователя. */
    public void applyContextDiffToStorage(ContextDiff diff) {
        AuditContext previous = AuditContextHolder.get();
        AuditContextHolder.set(new AuditContext(AuditContextHolder.SYSTEM_ACTOR, VIA));
        try {
            reconciliation.applyContextDiffToStorage(diff);
        } finally {
            AuditContextHolder.set(previous);
        }
    }
}
