package com.alligator.market.backend.provider.context.sheduler;

import com.alligator.market.backend.provider.context.adapter.ProfilesReconciliationAdapter;
import com.alligator.market.domain.provider.context.ProfilesReconciliation;
import com.alligator.market.domain.provider.context.ProfileContextDiff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Планировщик запускает методы сервиса {@link ProfilesReconciliation}.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProfilesReconciliationScheduler implements ApplicationRunner {

    private final ProfilesReconciliationAdapter reconciliationAdapter;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Start provider profiles reconciliation");
        ProfileContextDiff diff = reconciliationAdapter.compare();
        reconciliationAdapter.applyContextDiffToStorage(diff);
        log.info(
                "Provider profiles synced: added={}, replaced={}, missing={}",
                diff.add().size(),
                diff.replaced().size(),
                diff.missing().size()
        );
    }
}
