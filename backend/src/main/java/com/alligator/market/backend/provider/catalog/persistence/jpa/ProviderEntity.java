package com.alligator.market.backend.provider.catalog.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.backend.provider.catalog.persistence.jpa.descriptor.ProviderDescriptorEmbeddable;
import com.alligator.market.backend.provider.catalog.persistence.jpa.policy.ProviderPolicyEmbeddable;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.reconciliation.ProviderSynchronizer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * JPA-сущность провайдера рыночных данных.
 * Поле с кодом провайдера не обновляемое: логика синхронизации с контекстом приложения использует
 * стратегию delete → insert {@link ProviderSynchronizer}.
 */
@Entity
@Table(
        name = "market_data_provider",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_provider_code", columnNames = "provider_code")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProviderEntity extends BaseEntity {

    /** Суррогатный PK. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Технический код провайдера {@link MarketDataProvider#providerCode()}. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "provider_code", length = 50, nullable = false, updatable = false)
    private String providerCode;

    /** Иммутабельный дескриптор. */
    @Embedded
    private ProviderDescriptorEmbeddable descriptor;

    /** Иммутабельный набор параметров политики провайдера. */
    @Embedded
    private ProviderPolicyEmbeddable policy;

    ProviderEntity(
            String providerCode,
            ProviderDescriptorEmbeddable descriptor,
            ProviderPolicyEmbeddable policy
    ) {
        this.providerCode = Objects.requireNonNull(providerCode, "providerCode must not be null");
        this.descriptor = Objects.requireNonNull(descriptor, "descriptor must not be null");
        this.policy = Objects.requireNonNull(policy, "policy must not be null");
    }

    /** Фабрика для создания иммутабельной сущности провайдера. */
    public static ProviderEntity of(
            String providerCode,
            ProviderDescriptorEmbeddable descriptor,
            ProviderPolicyEmbeddable policy
    ) {
        return new ProviderEntity(providerCode, descriptor, policy);
    }
}
