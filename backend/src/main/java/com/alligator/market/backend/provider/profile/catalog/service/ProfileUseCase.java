package com.alligator.market.backend.provider.profile.catalog.service;

import com.alligator.market.domain.provider.model.info.ProviderStaticInfo;

import java.util.Map;

/**
 * Application-сервис (use case) для операций профилями провайдеров.
 */
public interface ProfileUseCase {

    /** Вернуть все активные профили вместе с PK. */
    Map<Long, ProviderStaticInfo> findAllActive();

    /** Вернуть все профили с их статусами. */
    Map<ProviderStaticInfo, ProfileStatus> findAllWithStatus();
}
