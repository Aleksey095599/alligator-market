package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.backend.config.audit.old.ServiceAuditorContext;
import com.alligator.market.domain.provider.context_sync.ContextDiff;
import com.alligator.market.domain.provider.context_sync.ProviderContextScanner;
import com.alligator.market.domain.provider.context_sync.ProviderProfilesReconciliation;
import com.alligator.market.domain.provider.profile.catalog.ProviderProfileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

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

    /** Применить {@link ContextDiff} к хранилищу данных, выполняя задачу от имени системного пользователя. */
    public void applyContextDiffToStorage(ContextDiff diff) {
        var domain = new ProviderProfilesReconciliation(contextScanner, profileStorage);
        Authentication previous = SecurityContextHolder.getContext().getAuthentication();
        var internal = new UsernamePasswordAuthenticationToken("internal-service", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(internal);
        try {
            domain.applyContextDiffToStorage(diff);
        } finally {
            SecurityContextHolder.getContext().setAuthentication(previous);
        }
    }
}
