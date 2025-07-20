package com.alligator.market.backend.provider.catalog.entity;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.backend.provider.catalog.service.ProviderCatalogSync;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.AccessMethod;
import com.alligator.market.domain.provider.DeliveryMode;
import com.alligator.market.domain.provider.ProviderCatalogStatus;
import com.alligator.market.domain.instrument.type.InstrumentType;
import java.util.Set;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity каталога провайдеров.
 * Каталог содержит записи о найденных в Spring-контексте бинах адаптеров провайдеров рыночных данных.
 * Поля каталога соответствуют статическим метаданным единого контракта адаптера: {@link MarketDataProvider}.
 * Логика заполнения и обновления таблицы задана в {@link ProviderCatalogSync#refresh()}.
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

    /**
     * Суррогатный PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Статус адаптера
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private ProviderCatalogStatus status;

    //==================================
    // Статические метаданные провайдера
    //==================================

    /**
     * Технический идентификатор провайдера.
     * Соответствует {@link MarketDataProvider#providerCode()}.
     */
    @Column(length = 50, nullable = false)
    private String providerCode;

    /**
     * Читаемое имя для UI/логов.
     * Соответствует {@link MarketDataProvider#displayName()}.
     */
    @Column(length = 50, nullable = false)
    private String displayName;

    /**
     * Поддерживаемые классы инструментов.
     * Соответствует {@link MarketDataProvider#instrumentTypes()}.
     */
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

    /**
     * Режим доставки рыночных данных: PULL или PUSH.
     * Соответствует {@link MarketDataProvider#deliveryMode()}.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private DeliveryMode deliveryMode;

    /**
     * Метод доступа к рыночным данным: API_POLL, WEBSOCKET, FIX или другие.
     * Соответствует {@link MarketDataProvider#accessMethod()}.
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AccessMethod accessMethod;

    /**
     * Возможна ли массовая подписка одним запросом.
     * Соответствует {@link MarketDataProvider#supportsBulkSubscription()}.
     */
    @Column(nullable = false)
    private boolean supportsBulkSubscription;

    /**
     * Минимально допустимый интервал опроса в миллисекундах.
     * Соответствует {@link MarketDataProvider#minPollPeriodMs()}.
     */
    @Column(name = "min_poll_period_ms", nullable = false)
    private int minPollPeriodMs;
}

