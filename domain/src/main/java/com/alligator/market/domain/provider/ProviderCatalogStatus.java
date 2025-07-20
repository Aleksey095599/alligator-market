package com.alligator.market.domain.provider;

/**
 * Статус адаптера в каталоге провайдеров.
 */
public enum ProviderCatalogStatus {

    // Бин соответствующего провайдера найден
    ACTIVE,

    // Бин соответствующего провайдера не найден, запись сохранена ради ссылок
    UNAVAILABLE
}

