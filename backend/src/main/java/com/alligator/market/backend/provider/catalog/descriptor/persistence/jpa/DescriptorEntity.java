package com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa;

import com.alligator.market.backend.provider.catalog.base.persistence.jpa.ProviderBaseEntity;
import com.alligator.market.domain.provider.contract.descriptor.AccessMethod;
import com.alligator.market.domain.provider.contract.descriptor.DeliveryMode;
import com.alligator.market.domain.provider.contract.descriptor.ProviderDescriptor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA-сущность дескриптора.
 * Набор полей аналогичен доменной модели {@link ProviderDescriptor}.
 */
@Entity
@Table(name = "provider_descriptor",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_display_name", columnNames = "display_name")
        })
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
public class DescriptorEntity extends ProviderBaseEntity {

    /** Отображаемое имя провайдера (user friendly) {@link ProviderDescriptor#displayName()}. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "display_name", length = 50, nullable = false)
    private String displayName;

    /** Режим доставки рыночных данных: PULL или PUSH {@link ProviderDescriptor#deliveryMode()}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_mode", length = 10, nullable = false)
    private DeliveryMode deliveryMode;

    /** Метод доступа к рыночным данным {@link ProviderDescriptor#accessMethod()}. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "access_method", length = 20, nullable = false)
    private AccessMethod accessMethod;

    /** Поддержка массовой подписки одним запросом {@link ProviderDescriptor#bulkSubscription()}. */
    @Column(name = "bulk_subscription", nullable = false)
    private boolean bulkSubscription;
}

