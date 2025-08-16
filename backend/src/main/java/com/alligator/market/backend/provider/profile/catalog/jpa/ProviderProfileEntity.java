package com.alligator.market.backend.provider.profile.catalog.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.backend.provider.profile.catalog.jpa.embedded.ProviderProfileEmbedded;
import com.alligator.market.domain.provider.profile.context.ProviderProfileStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity профиля провайдера рыночных данных.
 */
@Entity
@Table(name = "provider_profile")
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

    /** Статус профиля провайдера. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private ProviderProfileStatus status;

    /** Профиль провайдера. */
    @NotNull
    @Embedded
    private ProviderProfileEmbedded profile;
}

