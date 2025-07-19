package com.alligator.market.backend.provider.catalog.entity;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.AccessMethod;
import com.alligator.market.domain.provider.DeliveryMode;
import java.util.Set;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity для таблицы, в которой хранятся метаданные провайдеров, извлеченные из их адаптеров.
 * Поля соответствуют разделу "Статические метаданные провайдера" в едином контракте адаптера:
 * {@link MarketDataProvider}.
 */
@Entity
@Table(
        name = "provider_catalog",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_provider_code", columnNames = {"provider_code"}
                )
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

    /**
     * Уникальный код-идентификатор провайдера
     * согласно {@link MarketDataProvider#providerCode()}
     */
    @Column(length = 50, nullable = false)
    private String providerCode;

    /**
     * Режим доставки рыночных данных
     * согласно {@link MarketDataProvider#deliveryMode()}
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private DeliveryMode deliveryMode;

    /**
     * Метод доступа к рыночным данным
     * согласно {@link MarketDataProvider#accessMethod()}
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AccessMethod accessMethod;

    /**
     * Поддержка массовой подписки на инструменты
     * согласно {@link MarketDataProvider#supportsBulkSubscription()}
     */
    @Column(nullable = false)
    private boolean supportsBulkSubscription;

    /**
     * Поддерживаемые инструменты
     * согласно {@link MarketDataProvider#instrumentTypes()}
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "provider_catalog_instrument_types",
            joinColumns = @JoinColumn(name = "provider_catalog_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "instrument_type", length = 20, nullable = false)
    private Set<InstrumentType> instrumentTypes;
}
