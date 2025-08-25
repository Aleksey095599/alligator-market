package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.backend.provider.catalog.persistence.jpa.ProviderEntity;
import com.alligator.market.domain.provider.profile.model.ProviderAccessMethod;
import com.alligator.market.domain.provider.profile.model.ProviderDeliveryMode;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность профиля провайдера рыночных данных.
 * Дочерняя относительно {@link ProviderEntity}.
 */
@Entity
@Table(name = "provider_profile")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProviderProfileEntity extends ProviderEntity {

    /** Технический код провайдера {@link ProviderProfile#providerCode()}. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "provider_code", length = 50, nullable = false, updatable = false)
    private String providerCode;

    /** Отображаемое имя провайдера (user friendly) {@link ProviderProfile#displayName()}. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "display_name", length = 50, nullable = false, updatable = false)
    private String displayName;

    /** Режим доставки рыночных данных: PULL или PUSH {@link ProviderProfile#deliveryMode()}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_mode", length = 10, nullable = false, updatable = false)
    private ProviderDeliveryMode deliveryMode;

    /** Метод доступа к рыночным данным: API_POLL, WEBSOCKET, FIX или другие {@link ProviderProfile#accessMethod()}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "access_method", length = 20, nullable = false, updatable = false)
    private ProviderAccessMethod accessMethod;

    /** Поддержка массовой подписки одним запросом {@link ProviderProfile#bulkSubscription()}. */
    @Column(name = "bulk_subscription", nullable = false, updatable = false)
    private boolean bulkSubscription;

    /** Минимально допустимый интервал опроса в миллисекундах {@link ProviderProfile#minPollMs()}. */
    @Column(name = "min_poll_ms", nullable = false, updatable = false)
    private int minPollMs;
}

