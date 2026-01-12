package com.alligator.market.backend.provider.catalog.passport.service;

import com.alligator.market.backend.provider.catalog.passport.persistence.jpa.ProviderPassportEntity;
import com.alligator.market.backend.provider.catalog.passport.persistence.jpa.ProviderPassportJpaRepository;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
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

    private final ProviderPassportJpaRepository repository;

    @Override
    public List<ProviderCatalogItem> findAll() {
        return repository.findAll().stream()
                .map(this::toCatalogItem)
                .toList();
    }

    private ProviderCatalogItem toCatalogItem(ProviderPassportEntity entity) {
        Objects.requireNonNull(entity, "entity must not be null");

        ProviderPassportEmbeddable passportEmbeddable = Objects.requireNonNull(
                entity.getPassport(),
                "passport must not be null"
        );
        ProviderPassport passport = new ProviderPassport(
                passportEmbeddable.getDisplayName(),
                passportEmbeddable.getDeliveryMode(),
                passportEmbeddable.getAccessMethod(),
                passportEmbeddable.isBulkSubscription()
        );

        ProviderPolicyEmbeddable policyEmbeddable = Objects.requireNonNull(entity.getPolicy(), "policy must not be null");
        java.time.Duration minUpdateInterval = Objects.requireNonNull(
                policyEmbeddable.getMinUpdateInterval(),
                "minUpdateInterval must not be null"
        );
        ProviderPolicy policy = new ProviderPolicy(minUpdateInterval);

        return new ProviderCatalogItem(
                ProviderCode.of(entity.getProviderCode()),
                passport,
                policy
        );
    }
}
