package com.alligator.market.backend.provider.catalog.settings.service;

import com.alligator.market.domain.provider.contract.settings.ProviderSettings;

import java.util.List;

/**
 * Application-сервис (use case) для операций с настройками провайдеров.
 */
public interface ProviderSettingsUseCase {

    /** Вернуть все настройки. */
    List<ProviderSettings> getAll();
}
