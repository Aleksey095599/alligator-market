package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.domain.provider.profile.ProviderProfile;
import java.util.Map;

public record SyncResult(

        Map<ProviderProfile, Long> addNewWithActiveStatus,
        Map<ProviderProfile, Long> changeStatusToReplaced,
        Map<ProviderProfile, Long> changeStatusToMissing
) {}
