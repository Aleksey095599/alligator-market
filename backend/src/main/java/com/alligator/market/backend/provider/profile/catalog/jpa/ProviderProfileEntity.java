package com.alligator.market.backend.provider.profile.catalog.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.backend.provider.profile.catalog.jpa.embaddable.ProfileEmbeddable;
import com.alligator.market.domain.provider.model.ProviderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity провайдера рыночных данных (далее - провайдера).
 */
@Entity
@Table(name = "provider")
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

    /** Статус провайдера согласно {@link ProviderStatus}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private ProviderStatus status;

    /** Данные профиля провайдера. */
    @Embedded
    private ProfileEmbeddable profile;
}

