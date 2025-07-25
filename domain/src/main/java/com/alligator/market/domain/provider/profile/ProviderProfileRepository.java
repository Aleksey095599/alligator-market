package com.alligator.market.domain.provider.profile;

import java.util.List;

/**
 * Порт хранилища профилей провайдеров.
 */
public interface ProviderProfileRepository {

    /** Вернуть все профили провайдера */
    List<ProviderProfile> findAll();
}
