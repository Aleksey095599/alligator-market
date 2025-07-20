package com.alligator.market.domain.provider;

/**
 * Статус адаптера в каталоге провайдеров.
 */
public enum ProviderCatalogStatus {

    // Адаптер найден в коде и успешно синхронизирован
    FOUND,

    // В коде адаптера больше нет (или не прогрузился), запись сохранена ради ссылок
    NOT_FOUND
}

