package com.alligator.market.domain.provider;

/**
 * Статус адаптера в каталоге провайдеров.
 */
public enum ProviderCatalogStatus {

    /** Адаптер найден в коде и успешно синхронизирован. */
    ACTIVE,

    /** В коде адаптера больше нет (или не прогрузился), запись сохранена ради ссылок. */
    MISSING
}

