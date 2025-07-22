package com.alligator.market.domain.provider;

/**
 * Статус адаптера в каталоге провайдеров.
 */
public enum ProviderCatalogStatus {

    // Бин адаптера провайдера доступен в контексте Spring
    AVAILABLE,

    // Архивная запись (бин адаптера провайдера не был найден или был изменен)
    ARCHIVED
}

