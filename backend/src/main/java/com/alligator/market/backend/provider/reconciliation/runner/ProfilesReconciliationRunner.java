package com.alligator.market.backend.provider.reconciliation.runner;

import com.alligator.market.backend.provider.reconciliation.adapter.ProfilesReconcilerAdapter;
import com.alligator.market.domain.instrument.type.InstrumentType;
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

        // Извлекаем карту профилей провайдеров из контекста <providerCode, <displayName, Profile>>
        Map<String, Map<String, Profile>> contextProfiles = providerContextScanner.getProfiles();

        // Извлекаем карту обработчиков контекста <providerCode, <InstrumentType, InstrumentHandler>>
        Map<String, Map<InstrumentType, InstrumentHandler>> contextHandlers = providerContextScanner.getHandlers();

        // Проверяем корректность профилей и обработчиков
        ProfileValidator profileValidator = new ProfileValidator();
        HandlerValidator handlerValidator = new HandlerValidator();
        profileValidator.validateNoDuplicates(contextProfiles);
        log что валидация профилей успешно
        handlerValidator.validateHandlers(contextHandlers);
        log что валидация обработчиков успешно

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
