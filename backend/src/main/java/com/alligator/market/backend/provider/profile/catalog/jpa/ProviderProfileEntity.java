package com.alligator.market.backend.provider.profile.catalog.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.backend.provider.profile.catalog.jpa.embaddable.ProviderProfileEmbeddable;
import com.alligator.market.domain.provider.profile.model.ProviderProfileStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity профиля провайдера рыночных данных (далее - провайдера).
 */
@Entity
@Table(
        name = "provider_profile",
        uniqueConstraints = @UniqueConstraint(name = "uq_provider_profile_code", columnNames = "provider_code")
)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ProviderProfileEntity extends BaseEntity {

    /** Суррогатный PK. */
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Код провайдера, объединенный с версией записи. */
    @NotNull
    @Column(name = "provider_versioned_code", length = 10, nullable = false, updatable = false)
    private String providerVersionedCode;

    /** Статус профиля провайдера согласно {@link ProviderProfileStatus}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private ProviderProfileStatus status;

    /** Данные профиля провайдера. */
    @Embedded
    private ProviderProfileEmbeddable profile;


}

