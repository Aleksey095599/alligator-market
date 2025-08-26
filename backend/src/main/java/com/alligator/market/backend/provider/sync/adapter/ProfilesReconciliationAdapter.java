package com.alligator.market.backend.provider.sync.adapter;

import com.alligator.market.backend.config.audit.AuditContext;
import com.alligator.market.backend.config.audit.AuditContextHolder;
import com.alligator.market.domain.provider.profile.repository.ProfileRepository;
import com.alligator.market.domain.provider.reconciliation.ProfileDiff;
import com.alligator.market.domain.provider.reconciliation.ProfileContextScanner;
import com.alligator.market.domain.provider.reconciliation.ProfileReconciler;
import org.springframework.stereotype.Component;

/**
 * Компонент реализует доменную логику сопоставления профилей провайдеров рыночных данных,
 * извлеченных из контекста приложения и репозитория.
 */
@Component
public class ProfilesReconciliationAdapter implements ProfileReconciler {

    /** Внутренний источник операции. */
    private static final String via = "provider-profiles-reconciliation";

    /** Доменная логика сопоставления профилей. */
    private final ProfileReconciler reconciliation;

    // Конструктор
    public ProfilesReconciliationAdapter(
            ProfileContextScanner contextScanner,
            ProfileRepository repository
    ) {
        this.reconciliation = new ProfileReconciler(contextScanner, repository);
    }

    /** Сравнить профили и получить расхождения в виде {@link ProfileDiff}. */
    public ProfileDiff compareContextAndRepository() {
        return reconciliation.compareContextAndRepository();
    }

    /** Применить {@link ProfileDiff} к репозиторию, выполняя задачу от имени системного пользователя. */
    public void applyContextDiffToStorage(ProfileDiff diff) {
        AuditContext previous = AuditContextHolder.get();
        AuditContextHolder.set(new AuditContext(AuditContextHolder.SYSTEM_ACTOR, via));
        try {
            reconciliation.applyDiffToRepository(diff);
        } finally {
            AuditContextHolder.set(previous);
        }
    }
}
