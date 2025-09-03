package com.alligator.market.backend.provider.reconciliation.runner;

import com.alligator.market.backend.provider.reconciliation.adapter.ProfilesReconcilerAdapter;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
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

        // Извлекаем карту профилей провайдеров из контекста,
        // где первый ключ — код провайдера, второй — имя провайдера
        Map<String, Map<String, Profile>> contextProfiles = providerContextScanner.getProfiles();
        log.info("Retrieved profiles for providers with codes: {}", contextProfiles.keySet());

        // Извлекаем обработчики (handlers) финансовых инструментов,
        // где первый ключ — код провайдера, второй ключ — тип финансового инструмента
        Map<String, Map<InstrumentType, InstrumentHandler>> contextHandlers = providerContextScanner.getHandlers();
        log.info("Retrieved handlers for providers with codes: {}", contextHandlers.keySet());

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
