package com.alligator.market.backend.provider.profile.catalog.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.instrument.model.InstrumentType;
import java.util.Set;

import com.alligator.market.domain.provider.profile.model.AccessMethod;
import com.alligator.market.domain.provider.profile.model.DeliveryMode;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.profile.model.ProviderProfileStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity профиля провайдера рыночных данных (далее - провайдера).
 */
@Entity
@Table(name = "provider_profile")
@Getter
@Setter
@NoArgsConstructor
public class ProviderProfileEntity extends BaseEntity {

    /** Суррогатный PK. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Статус профиля провайдера согласно {@link ProviderProfileStatus}. */
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private ProviderProfileStatus status;

    //==========================
    // Данные профиля провайдера
    //==========================

    /** Технический код {@link ProviderProfile#providerCode()}. */
    @Column(length = 50, nullable = false)
    private String providerCode;

    /** Отображаемое имя {@link ProviderProfile#displayName()}. */
    @Column(length = 50, nullable = false)
    private String displayName;

    /** Поддерживаемые инструменты {@link ProviderProfile#instrumentTypes()}. */
    @ElementCollection(targetClass = InstrumentType.class)
    @CollectionTable(
            name = "provider_profile_instrument_type",
            joinColumns = @JoinColumn(
                    name = "provider_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_provider_instrument_type_provider")
            )
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "instrument_type", length = 20, nullable = false)
    private Set<InstrumentType> instrumentTypes;

    /** Режим доставки рыночных данных: PULL или PUSH {@link ProviderProfile#deliveryMode()}. */
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private DeliveryMode deliveryMode;

    /** Метод доступа к рыночным данным: API_POLL, WEBSOCKET, FIX или другие {@link ProviderProfile#accessMethod()}. */
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AccessMethod accessMethod;

    /** Поддержка массовой подписки одним запросом {@link ProviderProfile#supportsBulkSubscription()}. */
    @Column(nullable = false)
    private boolean supportsBulkSubscription;

    /** Минимально допустимый интервал опроса в миллисекундах {@link ProviderProfile#minPollPeriodMs()}. */
    @Column(name = "min_poll_period_ms", nullable = false)
    private int minPollPeriodMs;
}

