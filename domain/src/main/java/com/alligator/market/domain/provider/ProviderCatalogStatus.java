package com.alligator.market.domain.provider;

/**
 * Статус адаптера в каталоге провайдеров.
 */
public enum ProviderCatalogStatus {

    // Бин соответствующего адаптера найден
    IMPLEMENTED,

    // Бин соответствующего адаптера не найден, запись сохранена ради ссылок
    NOT_IMPLEMENTED
}

