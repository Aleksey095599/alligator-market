package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.domain.provider.context_sync.ContextDiff;
import com.alligator.market.domain.provider.context_sync.ProviderContextScanner;
import com.alligator.market.domain.provider.context_sync.ProviderProfilesReconciliation;
import com.alligator.market.domain.provider.profile.ProviderProfileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Адаптер вызывает доменную логику сравнения профилей.
 */
@Component
@RequiredArgsConstructor
public class ProviderProfilesReconciliationAdapter {

    private final ProviderContextScanner contextScanner;
    private final ProviderProfileStorage profileStorage;

    /** Сравнить профили и получить diff. */
    public ContextDiff compare() {
        var domain = new ProviderProfilesReconciliation(contextScanner, profileStorage);
        return domain.compare();
    }

    /** Применить diff к хранилищу. */
    public void applyContextDiffToStorage(ContextDiff diff) {
        var domain = new ProviderProfilesReconciliation(contextScanner, profileStorage);
        domain.applyContextDiffToStorage(diff);
    }
}
