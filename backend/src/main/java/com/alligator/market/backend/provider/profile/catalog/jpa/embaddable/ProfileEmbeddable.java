package com.alligator.market.backend.provider.profile.catalog.jpa.embaddable;

import com.alligator.market.domain.instrument.model.InstrumentType;
import com.alligator.market.domain.provider.profile.model.AccessMethod;
import com.alligator.market.domain.provider.profile.model.DeliveryMode;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Встраиваемая часть профиля провайдера рыночных данных.
 * Поля полностью соответствуют параметром доменной модели {@link ProviderProfile}
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ProfileEmbeddable {

    /** Технический код {@link ProviderProfile#providerCode()}. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "provider_code", length = 50, nullable = false, updatable = false)
    private String providerCode;

    /** Отображаемое имя {@link ProviderProfile#displayName()}. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "display_name", length = 50, nullable = false)
    private String displayName;

    /** Поддерживаемые инструменты {@link ProviderProfile#instrumentTypes()}. */
    @ElementCollection(targetClass = InstrumentType.class)
    @CollectionTable(
            name = "provider_profile_instrument_type",
            joinColumns = @JoinColumn(
                    name = "provider_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(name = "fk_provider_profile_instrument_type_provider_profile")
            )
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "instrument_type", length = 20, nullable = false)
    private Set<InstrumentType> instrumentTypes;

    /** Режим доставки рыночных данных: PULL или PUSH {@link ProviderProfile#deliveryMode()}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_mode", length = 10, nullable = false)
    private DeliveryMode deliveryMode;

    /** Метод доступа к рыночным данным: API_POLL, WEBSOCKET, FIX или другие {@link ProviderProfile#accessMethod()}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "access_method", length = 20, nullable = false)
    private AccessMethod accessMethod;

    /** Поддержка массовой подписки одним запросом {@link ProviderProfile#supportsBulkSubscription()}. */
    @Column(name = "supports_bulk_subscription", nullable = false)
    private boolean supportsBulkSubscription;

    /** Минимально допустимый интервал опроса в миллисекундах {@link ProviderProfile#minPollPeriodMs()}. */
    @Column(name = "min_poll_period_ms", nullable = false)
    private int minPollPeriodMs;
}
