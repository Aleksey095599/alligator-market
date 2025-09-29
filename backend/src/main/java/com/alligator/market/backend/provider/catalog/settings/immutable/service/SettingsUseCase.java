package com.alligator.market.backend.provider.catalog.settings.immutable.service;

import com.alligator.market.domain.provider.contract.settings.immutable.ProviderSettings;

import java.util.List;

/**
 * Application-сервис (use case) для операций с настройками провайдеров.
 */
public interface SettingsUseCase {

    /** Вернуть все настройки. */
    List<ProviderSettings> getAll();
}
