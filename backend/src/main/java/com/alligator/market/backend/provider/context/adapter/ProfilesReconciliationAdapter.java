package com.alligator.market.backend.provider.context.adapter;

import com.alligator.market.backend.config.audit.AuditContext;
import com.alligator.market.backend.config.audit.AuditContextHolder;
import com.alligator.market.domain.provider.profile.context.ProfileContextDiff;
import com.alligator.market.domain.provider.profile.context.ProfileContextScanner;
import com.alligator.market.domain.provider.profile.context.ProfilesReconciliation;
import com.alligator.market.domain.provider.profile.catalog.ProviderProfileStorage;
import org.springframework.stereotype.Component;

/**
 * Компонент вызывает доменную логику сопоставления профилей провайдеров рыночных данных,
 * извлеченных из контекста приложения и хранилища данных.
 */
@Component
public class ProfilesReconciliationAdapter {

    /** Внутренний источник операции. */
    private static final String VIA = "provider-profiles-reconciliation";

    /** Доменная логика сопоставления профилей. */
    private final ProfilesReconciliation reconciliation;

    // Конструктор
    public ProfilesReconciliationAdapter(
            ProfileContextScanner contextScanner,
            ProviderProfileStorage profileStorage
    ) {
        this.reconciliation = new ProfilesReconciliation(contextScanner, profileStorage);
    }

    /** Сравнить профили и получить расхождения в виде {@link ProfileContextDiff}. */
    public ProfileContextDiff compare() {
        return reconciliation.compare();
    }

    /** Применить {@link ProfileContextDiff} к хранилищу данных, выполняя задачу от имени системного пользователя. */
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
