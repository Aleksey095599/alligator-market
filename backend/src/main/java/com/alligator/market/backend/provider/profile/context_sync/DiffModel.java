package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.backend.provider.profile.entity.ProviderProfileEntity;

import java.util.List;

public record DiffModel (

        List<ProviderProfileEntity> toActive,
        List<ProviderProfileEntity> toReplaced,
        List<ProviderProfileEntity> toMissing
) {}
