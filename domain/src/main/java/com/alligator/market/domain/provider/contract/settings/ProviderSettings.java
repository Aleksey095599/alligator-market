package com.alligator.market.domain.provider.contract.settings;

/**
 * Настройки провайдера: параметры, которые разрешено менять из frontend (read/write).
 * Пока что это заглушка: отдельные реализации появятся, когда будут реальными управляемыми параметрами.
 */
public interface ProviderSettings {

    /**
     * Возвращает пустой набор настроек (заглушка по умолчанию).
     */
    static ProviderSettings empty() {
        return EmptyProviderSettings.INSTANCE;
    }

    /**
     * Заглушка без параметров.
     */
    final class EmptyProviderSettings implements ProviderSettings {

        private static final EmptyProviderSettings INSTANCE = new EmptyProviderSettings();

        private EmptyProviderSettings() {
        }
    }
}
