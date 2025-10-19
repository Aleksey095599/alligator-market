package com.alligator.market.domain.provider.reconciliation.dto;

import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;

/** Снимок провайдера для процесса синхронизации. */
public record ProviderSnapshot(
        String code,
        ProviderDescriptor descriptor,
        ProviderPolicy policy
) {}
