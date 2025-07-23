package com.alligator.market.backend.provider.catalog.entity;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.provider.*;
import com.alligator.market.domain.instrument.type.InstrumentType;
import java.util.Set;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity для каталога провайдеров рыночных данных.
 * Поля Entity соответствуют статическим метаданным единого контракта адаптера: {@link MarketDataProvider}.
 */
@Entity
@Table(
        name = "provider_catalog",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_provider_code", columnNames = "provider_code"),
                @UniqueConstraint(name = "uq_display_name", columnNames = "display_name")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ProviderCatalogEntity extends BaseEntity {

    /** Суррогатный PK */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Статус адаптера */
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private ProviderCatalogStatus status;

    //========================
    // Поля профиля провайдера
    //========================

    /** {@link ProviderProfile#providerCode()} */
    @Column(length = 50, nullable = false)
    private String providerCode;

    /** {@link ProviderProfile#displayName()} */
    @Column(length = 50, nullable = false)
    private String displayName;

    /** {@link ProviderProfile#instrumentTypes()} */
    @ElementCollection(targetClass = InstrumentType.class)
    @CollectionTable(
            name = "provider_catalog_instrument_type",
            joinColumns = @JoinColumn(
                    name = "provider_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_provider_instrument_type_provider")
            )
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "instrument_type", length = 20, nullable = false)
    private Set<InstrumentType> instrumentTypes;

    /** {@link ProviderProfile#deliveryMode()} */
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private DeliveryMode deliveryMode;

    /** {@link ProviderProfile#accessMethod()} */
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AccessMethod accessMethod;

    /** {@link ProviderProfile#supportsBulkSubscription()} */
    @Column(nullable = false)
    private boolean supportsBulkSubscription;

    /** {@link ProviderProfile#minPollPeriodMs()} */
    @Column(name = "min_poll_period_ms", nullable = false)
    private int minPollPeriodMs;
}

