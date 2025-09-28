package com.alligator.market.backend.provider.catalog.settings.immutable.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.provider.contract.settings.immutable.ProviderSettings;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA-сущность настроек провайдера.
 * Набор полей аналогичен доменной модели {@link ProviderSettings}.
 */
@Entity
@Table(
        name = "provider_settings",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_provider_settings_provider_code", columnNames = "provider_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ProviderSettingsEntity extends BaseEntity {

    /** Суррогатный PK. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Технический код провайдера {@link ProviderSettings#providerCode()}. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "provider_code", length = 50, nullable = false, updatable = false)
    private String providerCode;

    /** Минимальный интервал обновления котировки в секундах {@link ProviderSettings#minUpdateInterval()}. */
    @NotNull
    @Min(1)
    @Column(name = "min_update_interval_seconds", nullable = false, updatable = false)
    private Long minUpdateIntervalSeconds;
}
