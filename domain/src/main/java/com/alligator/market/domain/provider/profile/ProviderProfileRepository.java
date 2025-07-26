package com.alligator.market.domain.provider.profile;

import java.util.Map;

/**
 * Порт хранилища профилей провайдеров.
 */
public interface ProviderProfileRepository {

    /** Вернуть все активные профили провайдеров вместе с PK */
    Map<ProviderProfile, Long> findAllActive();
}
