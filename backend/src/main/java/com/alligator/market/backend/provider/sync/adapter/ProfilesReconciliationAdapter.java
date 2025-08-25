package com.alligator.market.backend.provider.sync.adapter;

import com.alligator.market.backend.config.audit.AuditContext;
import com.alligator.market.backend.config.audit.AuditContextHolder;
import com.alligator.market.domain.provider.model.ProfileContextDiff;
import com.alligator.market.domain.provider.contract.ProviderContextScanner;
import com.alligator.market.domain.provider.service.ProviderSyncService;
import org.springframework.stereotype.Component;

/**
 * Компонент вызывает доменную логику сопоставления профилей провайдеров рыночных данных,
 * извлеченных из контекста приложения и репозитория.
 */
@Component
public class ProfilesReconciliationAdapter {

    /** Внутренний источник операции. */
    private static final String VIA = "provider-profiles-reconciliation";

    /** Доменная логика сопоставления профилей. */
    private final ProviderSyncService reconciliation;

    // Конструктор
    public ProfilesReconciliationAdapter(
            ProviderContextScanner contextScanner,
            ProviderProfileStorage profileStorage
    ) {
        this.reconciliation = new ProviderSyncService(contextScanner, profileStorage);
    }

    /** Сравнить профили и получить расхождения в виде {@link ProfileContextDiff}. */
    public ProfileContextDiff compare() {
        return reconciliation.compare();
    }

    /** Применить {@link ProfileContextDiff} к репозиторию, выполняя задачу от имени системного пользователя. */
    public void applyContextDiffToStorage(ProfileContextDiff diff) {
        AuditContext previous = AuditContextHolder.get();
        AuditContextHolder.set(new AuditContext(AuditContextHolder.SYSTEM_ACTOR, VIA));
        try {
            reconciliation.applyContextDiffToStorage(diff);
        } finally {
            AuditContextHolder.set(previous);
        }
    }
}
