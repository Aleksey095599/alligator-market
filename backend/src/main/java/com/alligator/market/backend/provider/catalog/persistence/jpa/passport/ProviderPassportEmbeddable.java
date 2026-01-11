package com.alligator.market.backend.provider.catalog.persistence.jpa.passport;

import com.alligator.market.domain.provider.contract.passport.AccessMethod;
import com.alligator.market.domain.provider.contract.passport.DeliveryMode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Embeddable JPA-сущность паспорта провайдера.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProviderPassportEmbeddable {

    /**
     * Отображаемое имя провайдера (user friendly).
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = "display_name", length = 50, nullable = false, updatable = false)
    private String displayName;

    /**
     * Режим доставки рыночных данных: PULL или PUSH.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_mode", length = 10, nullable = false, updatable = false)
    private DeliveryMode deliveryMode;

    /**
     * Метод доступа к рыночным данным.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "access_method", length = 20, nullable = false, updatable = false)
    private AccessMethod accessMethod;

    /**
     * Поддержка массовой подписки одним запросом.
     */
    @Column(name = "bulk_subscription", nullable = false, updatable = false)
    private boolean bulkSubscription;
}
