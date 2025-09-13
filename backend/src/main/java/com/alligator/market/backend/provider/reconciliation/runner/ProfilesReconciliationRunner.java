package com.alligator.market.backend.provider.reconciliation.runner;

import com.alligator.market.backend.provider.reconciliation.adapter.ProfilesReconcilerAdapter;
import com.alligator.market.domain.instrument.base.contract.Instrument;
import com.alligator.market.domain.provider.model.handler.InstrumentHandler;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.model.info.ProviderStaticInfo;
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
 * Раннер процедуры сопоставления профилей провайдеров из контекста приложения и репозитория.
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

        log.info("Start provider profiles reconciliation");

        // Извлекаем список профилей провайдеров из контекста
        List<ProviderStaticInfo> contextProviderStaticInfos = providerContextScanner.getProfiles();
        // Проверяем, что нет дублирования по ProviderStaticInfo.providerCode и ProviderStaticInfo.displayName
        profileValidator.validateNoDuplicates(contextProviderStaticInfos);

        // Извлекаем список обработчиков из контекста
        // TODO: вероятно не нужен метод getHandlers так как теперь есть абстрактная модель провайдера, в которой есть карта обработчиков
        List<InstrumentHandler<? extends MarketDataProvider, ? extends Instrument>> contextHandlers = providerContextScanner.getHandlers();

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
