package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.domain.provider.profile.ProviderProfile;

import java.util.List;
import java.util.Map;

/**
 * Модель содержит списки профилей, касательно которых требуются действия в базе данных для обеспечения
 * синхронизации профилей провайдеров получаемых методом {@link ProviderContextScanner#getProviderProfiles()} и
 * профилей провайдеров, содержащихся в базе данных.
 *
 * @param addNewWithActiveStatus
 * @param changeStatusToReplaced
 * @param changeStatusToMissing
 */
public record ContextDiff(

        List<ProviderProfile> addNewWithActiveStatus,
        Map<ProviderProfile, Long> changeStatusToReplaced,
        Map<ProviderProfile, Long> changeStatusToMissing
) {}
