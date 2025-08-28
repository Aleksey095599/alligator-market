package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
import com.alligator.market.domain.instrument.type.InstrumentType;
import com.alligator.market.domain.provider.profile.model.AccessMethod;
import com.alligator.market.domain.provider.profile.model.DeliveryMode;
import com.alligator.market.domain.provider.profile.model.Profile;
import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Entity профиля провайдера.
 */
@Entity
@Table(name = "provider_profile")
@Getter
@Setter
@NoArgsConstructor
public class ProfileEntity extends BaseEntity {

    /** Суррогатный PK. */
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Статус профиля {@link Profile#profileStatus()}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "profile_status", length = 10, nullable = false)
    private ProfileStatus profileStatus;

    /** Технический код провайдера {@link Profile#providerCode()}. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "provider_code", length = 50, nullable = false, updatable = false)
    private String providerCode;

    /** Отображаемое имя провайдера (user friendly) {@link Profile#displayName()}. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "display_name", length = 50, nullable = false, updatable = false)
    private String displayName;

    /** Поддерживаемые инструменты {@link Profile#instrumentsSupported()}. */
    @ElementCollection(targetClass = InstrumentType.class)
    @CollectionTable(
            name = "profile_supported_instrument",
            joinColumns = @JoinColumn(
                    name = "profile_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_profile_supported_instrument")
            )
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "instruments_supported", length = 20, nullable = false, updatable = false)
    private Set<InstrumentType> instrumentsSupported;

    /** Режим доставки рыночных данных: PULL или PUSH {@link Profile#deliveryMode()}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_mode", length = 10, nullable = false, updatable = false)
    private DeliveryMode deliveryMode;

    /** Метод доступа к рыночным данным {@link Profile#accessMethod()}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "access_method", length = 20, nullable = false, updatable = false)
    private AccessMethod accessMethod;

    /** Поддержка массовой подписки одним запросом {@link Profile#bulkSubscription()}. */
    @Column(name = "bulk_subscription", nullable = false, updatable = false)
    private boolean bulkSubscription;

    /** Минимально допустимый интервал опроса в миллисекундах {@link Profile#minPollMs()}. */
    @Column(name = "min_poll_ms", nullable = false, updatable = false)
    private int minPollMs;
}

