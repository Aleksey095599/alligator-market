package com.alligator.market.backend.provider.catalog.entity;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.provider.MarketDataProvider;
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

    /* Суррогатный PK */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Уникальный код-идентификатор провайдера
     * согласно {@link MarketDataProvider#providerCode()}
     */
    @Column(length = 50, nullable = false)
    String providerCode;


}
