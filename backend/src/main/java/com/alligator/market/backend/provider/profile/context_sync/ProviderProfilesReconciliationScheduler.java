package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.domain.provider.context_sync.ContextDiff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Планировщик запуска сопоставления профилей провайдеров при старте приложения.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProviderProfilesReconciliationScheduler implements ApplicationRunner {

    private final ProviderProfilesReconciliationAdapter reconciliationAdapter;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Start provider profiles reconciliation");
        ContextDiff diff = reconciliationAdapter.compare();
        reconciliationAdapter.applyContextDiffToStorage(diff);
        log.info(
                "Provider profiles synced: added={}, replaced={}, missing={}",
                diff.add().size(),
                diff.replaced().size(),
                diff.missing().size()
        );
    }
}
