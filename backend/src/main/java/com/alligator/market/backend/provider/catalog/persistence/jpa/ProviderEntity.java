package com.alligator.market.backend.provider.catalog.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.instrument.model.InstrumentType;
import com.alligator.market.domain.provider.model.ProviderProfileStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Сущность провайдера рыночных данных.
 * Родитель для сущности профиля провайдера.
 */
@Entity
@Table(name = "provider")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class ProviderEntity extends BaseEntity {

    /** Суррогатный PK. */
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Поддерживаемые инструменты. */
    @ElementCollection(targetClass = InstrumentType.class)
    @CollectionTable(
            name = "provider_supported_instrument",
            joinColumns = @JoinColumn(
                    name = "provider_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_provider_supported_instrument")
            )
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "instruments_supported", length = 20, nullable = false, updatable = false)
    private Set<InstrumentType> instrumentsSupported;

    /** Статус профиля провайдера. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "profile_status", length = 10, nullable = false)
    private ProviderProfileStatus profile_status;
}
