package com.alligator.market.backend.provider.reconciliation.runner;

import com.alligator.market.backend.provider.reconciliation.adapter.ProfilesReconcilerAdapter;
import com.alligator.market.domain.provider.reconciliation.ProfileDiff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Раннер процедуры сопоставления профилей провайдеров рыночных данных,
 * извлеченных из контекста приложения и репозитория.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProfilesReconciliationRunner implements ApplicationRunner {

    private final ProfilesReconcilerAdapter reconciliationAdapter;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Start provider profiles reconciliation");
        ProfileDiff diff = reconciliationAdapter.compareContextAndRepository();
        reconciliationAdapter.applyContextDiffToStorage(diff);
        log.info(
                "Provider profiles synced: added={}, replaced={}, missing={}",
                diff.add().size(),
                diff.replaced().size(),
                diff.missing().size()
        );
    }
}
