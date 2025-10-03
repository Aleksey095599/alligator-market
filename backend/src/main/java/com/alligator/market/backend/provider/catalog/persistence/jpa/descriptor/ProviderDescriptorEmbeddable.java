package com.alligator.market.backend.provider.catalog.persistence.jpa.descriptor;

import com.alligator.market.domain.provider.contract.descriptor.AccessMethod;
import com.alligator.market.domain.provider.contract.descriptor.DeliveryMode;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import com.alligator.market.domain.provider.reconciliation.ProviderSynchronizer;
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

import java.util.Objects;

/**
 * Embeddable JPA-сущность дескриптора.
 * Набор полей аналогичен доменной модели {@link ProviderDescriptor}.
 * Все поля не обновляемые: логика синхронизации с контекстом приложения использует
 * стратегию delete → insert {@link ProviderSynchronizer}.
 */
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProviderDescriptorEmbeddable {

    /** Отображаемое имя провайдера (user friendly) {@link ProviderDescriptor#displayName()}. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "display_name", length = 50, nullable = false, updatable = false)
    private String displayName;

    /** Режим доставки рыночных данных: PULL или PUSH {@link ProviderDescriptor#deliveryMode()}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_mode", length = 10, nullable = false, updatable = false)
    private DeliveryMode deliveryMode;

    /** Метод доступа к рыночным данным {@link ProviderDescriptor#accessMethod()}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "access_method", length = 20, nullable = false, updatable = false)
    private AccessMethod accessMethod;

    /** Поддержка массовой подписки одним запросом {@link ProviderDescriptor#bulkSubscription()}. */
    @Column(name = "bulk_subscription", nullable = false, updatable = false)
    private boolean bulkSubscription;

    /** Конструктор. */
    ProviderDescriptorEmbeddable(
            String displayName,
            DeliveryMode deliveryMode,
            AccessMethod accessMethod,
            boolean bulkSubscription
    ) {
        this.displayName = Objects.requireNonNull(displayName, "displayName must not be null");
        this.deliveryMode = Objects.requireNonNull(deliveryMode, "deliveryMode must not be null");
        this.accessMethod = Objects.requireNonNull(accessMethod, "accessMethod must not be null");
        this.bulkSubscription = bulkSubscription;
    }

    /** Фабрика создания иммутабельного embeddable из доменного дескриптора. */
    public static ProviderDescriptorEmbeddable from(ProviderDescriptor descriptor) {
        Objects.requireNonNull(descriptor, "descriptor must not be null");
        return new ProviderDescriptorEmbeddable(
                descriptor.displayName(),
                descriptor.deliveryMode(),
                descriptor.accessMethod(),
                descriptor.bulkSubscription()
        );
    }
}
