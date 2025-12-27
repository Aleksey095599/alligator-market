package com.alligator.market.backend.provider.catalog.service;

import com.alligator.market.backend.provider.catalog.persistence.jpa.ProviderEntity;
import com.alligator.market.backend.provider.catalog.persistence.jpa.ProviderJpaRepository;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Реализация сервиса {@link ProviderCatalogUseCase}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProviderCatalogService implements ProviderCatalogUseCase {

    private final ProviderJpaRepository repository;

    @Override
    public List<ProviderCatalogItem> findAll() {
        return repository.findAll().stream()
                .map(this::toCatalogItem)
                .toList();
    }

    private ProviderCatalogItem toCatalogItem(ProviderEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null");

        var descriptorEmbeddable = Objects.requireNonNull(
                entity.getDescriptor(),
                "descriptor must not be null"
        );
        var descriptor = new ProviderDescriptor(
                descriptorEmbeddable.getDisplayName(),
                descriptorEmbeddable.getDeliveryMode(),
                descriptorEmbeddable.getAccessMethod(),
                descriptorEmbeddable.isBulkSubscription()
        );

        var policyEmbeddable = Objects.requireNonNull(entity.getPolicy(), "policy must not be null");
        var minUpdateInterval = Objects.requireNonNull(
                policyEmbeddable.getMinUpdateInterval(),
                "minUpdateInterval must not be null"
        );
        var policy = new ProviderPolicy(minUpdateInterval);

        return new ProviderCatalogItem(
                entity.getProviderCode(),
                descriptor,
                policy
        );
    }
}
