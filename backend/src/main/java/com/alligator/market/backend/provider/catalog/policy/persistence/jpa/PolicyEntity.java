package com.alligator.market.backend.provider.catalog.policy.persistence.jpa;

import com.alligator.market.backend.provider.catalog.base.jpa.ProviderBaseEntity;
import com.alligator.market.domain.provider.contract.policy.ProviderPolicy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA-сущность политики провайдера.
 * Набор полей аналогичен доменной модели {@link ProviderPolicy}.
 */
@Entity
@Table(name = "provider_policy")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
public class PolicyEntity extends ProviderBaseEntity {

    /** Минимальный интервал обновления котировок (в секундах) {@link ProviderPolicy#minUpdateInterval()}. */
    @Positive
    @Column(name = "min_update_interval_seconds", nullable = false)
    private long minUpdateIntervalSeconds;
}
