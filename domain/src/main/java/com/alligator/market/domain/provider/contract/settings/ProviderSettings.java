package com.alligator.market.domain.provider.contract.settings;

/**
 * Настройки провайдера: параметры, которые разрешено менять из frontend.
 */
public interface ProviderSettings {

    /**
     * Возвращает заглушку.
     */
    static ProviderSettings empty() {
        return EmptyProviderSettings.INSTANCE;
    }

    /**
     * Заглушка.
     */
    final class EmptyProviderSettings implements ProviderSettings {

        private static final EmptyProviderSettings INSTANCE = new EmptyProviderSettings();
        private EmptyProviderSettings() {}
    }
}
