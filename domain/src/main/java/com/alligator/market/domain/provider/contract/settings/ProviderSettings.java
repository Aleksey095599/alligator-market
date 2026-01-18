package com.alligator.market.domain.provider.contract.settings;

/**
 * Настройки провайдера: параметры, используемые бизнес логикой, которые разрешено менять пользователям приложения.
 */
public interface ProviderSettings {

    /**
     * Возвращает заглушку.
     */
    static ProviderSettings empty() {
        return EmptyProviderSettings.INSTANCE;
    }

    /**
     * Временный класс заглушка (TODO: реализовать).
     */
    final class EmptyProviderSettings implements ProviderSettings {

        private static final EmptyProviderSettings INSTANCE = new EmptyProviderSettings();

        private EmptyProviderSettings() {
        }
    }
}
