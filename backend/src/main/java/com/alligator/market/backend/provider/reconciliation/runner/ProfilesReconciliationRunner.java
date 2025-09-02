package com.alligator.market.backend.provider.reconciliation.runner;

import com.alligator.market.backend.provider.reconciliation.adapter.ProfilesReconcilerAdapter;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.reconciliation.ProviderContextScanner;
import com.alligator.market.domain.provider.reconciliation.ProfileDiff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Раннер процедуры сопоставления профилей провайдеров рыночных данных,
 * извлеченных из контекста приложения и репозитория.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProfilesReconciliationRunner implements ApplicationRunner {

    // Адаптер доменного сервиса сопоставления профилей провайдеров
    private final ProfilesReconcilerAdapter reconciliationAdapter;
    // Сканер контекста провайдеров
    private final ProviderContextScanner providerContextScanner;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Start provider profiles reconciliation");

        // Извлекаем карту профилей провайдеров из контекста, где ключ — код провайдера
        Map<String, Profile> contextProfiles = providerContextScanner.getProfiles();
        // Логируем коды провайдеров, для которых извлечены профили
        log.info(
                "Retrieved profiles for providers with codes: {}",
                contextProfiles.keySet()
        );

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
