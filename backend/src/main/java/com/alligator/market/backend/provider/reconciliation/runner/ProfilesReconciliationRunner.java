package com.alligator.market.backend.provider.reconciliation.runner;

import com.alligator.market.backend.provider.reconciliation.adapter.ProfilesReconcilerAdapter;
import com.alligator.market.domain.provider.handler.contract.InstrumentHandler;
import com.alligator.market.domain.provider.handler.service.HandlerValidator;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.service.ProfileValidator;
import com.alligator.market.domain.provider.reconciliation.ProviderContextScanner;
import com.alligator.market.domain.provider.reconciliation.ProfileDiff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

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
        ProfileValidator profileValidator = new ProfileValidator();
        HandlerValidator handlerValidator = new HandlerValidator();

        log.info("Start provider profiles reconciliation");

        // Извлекаем список профилей провайдеров из контекста
        List<Profile> contextProfiles = providerContextScanner.getProfiles();
        // Проверяем, что нет дублирования по Profile.providerCode и Profile.displayName
        profileValidator.validateNoDuplicates(contextProfiles);

        // Извлекаем список обработчиков из контекста
        List<InstrumentHandler> contextHandlers = providerContextScanner.getHandlers();
        handlerValidator.validateHandlers(contextHandlers);


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
