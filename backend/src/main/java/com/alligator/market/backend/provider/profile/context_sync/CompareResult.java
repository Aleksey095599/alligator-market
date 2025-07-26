package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.domain.provider.profile.ProviderProfile;

import java.util.List;
import java.util.Map;

/**
 * Результат сравнения профилей провайдеров из контекста Spring и базы данных.
 *
 * @param addNewWithActiveStatus
 * @param changeStatusToReplaced
 * @param changeStatusToMissing
 */
public record CompareResult(

        List<ProviderProfile> addNewWithActiveStatus,
        Map<ProviderProfile, Long> changeStatusToReplaced,
        Map<ProviderProfile, Long> changeStatusToMissing
) {}
