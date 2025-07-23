package com.alligator.market.domain.provider;

/**
 * Статус адаптера в профиле провайдера.
*/
public enum ProviderProfileStatus {

    // Бин адаптера провайдера доступен в контексте Spring
    AVAILABLE,

    // Архивная запись (бин адаптера провайдера не был найден или был изменен)
    ARCHIVED
}

