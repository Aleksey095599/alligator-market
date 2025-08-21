package com.alligator.market.backend.provider.sync.adapter;

import com.alligator.market.backend.config.audit.AuditContext;
import com.alligator.market.backend.config.audit.AuditContextHolder;
import com.alligator.market.domain.provider.sync.model.ProfileContextDiff;
import com.alligator.market.domain.provider.sync.contract.ProviderContextScanner;
import com.alligator.market.domain.provider.sync.service.ProviderReconciliationService;
import com.alligator.market.domain.provider.profile.contract.ProviderProfileStorage;
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
    private final ProviderReconciliationService reconciliation;

    // Конструктор
    public ProfilesReconciliationAdapter(
            ProviderContextScanner contextScanner,
            ProviderProfileStorage profileStorage
    ) {
        this.reconciliation = new ProviderReconciliationService(contextScanner, profileStorage);
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
