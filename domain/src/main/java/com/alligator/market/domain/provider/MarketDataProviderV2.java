package com.alligator.market.domain.provider;

import com.alligator.market.domain.provider.profile.ProviderProfile;

/**
 * Единый контракт адаптера для всех провайдеров рыночных данных.
 * Версия 2.
 */
public interface MarketDataProviderV2 {

    /** Уникальный профиль провайдера. */
    ProviderProfile profile();

    /** Текущий health-state. */
    default ProviderHealth health() { return ProviderHealth.UNKNOWN; }

    /** Открыть соединение. */
    default void connect() { /* no-op */ }

    /** Закрыть соединение. */
    default void disconnect() { /* no-op */ }
}
