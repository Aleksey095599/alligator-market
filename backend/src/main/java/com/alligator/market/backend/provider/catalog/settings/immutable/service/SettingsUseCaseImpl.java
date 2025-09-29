package com.alligator.market.backend.provider.catalog.settings.immutable.service;

import com.alligator.market.domain.provider.contract.settings.immutable.ProviderSettings;
import com.alligator.market.domain.provider.repository.ProviderSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса {@link SettingsUseCase}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SettingsUseCaseImpl implements SettingsUseCase {

    private final ProviderSettingsRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<ProviderSettings> getAll() {
        List<ProviderSettings> settings = repository.findAll();
        log.debug("Found {} provider settings", settings.size());
        return settings;
    }
}
