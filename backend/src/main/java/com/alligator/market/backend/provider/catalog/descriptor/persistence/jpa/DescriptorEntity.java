package com.alligator.market.backend.provider.catalog.descriptor.persistence.jpa;

import com.alligator.market.backend.common.jpa.BaseEntity;
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
@Table(
        name = "provider_descriptor",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_provider_descriptor_provider_code", columnNames = "provider_code"),
                @UniqueConstraint(name = "uq_provider_descriptor_display_name", columnNames = "display_name")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class DescriptorEntity extends BaseEntity {

    /** Суррогатный PK. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Технический код провайдера {@link com.alligator.market.domain.provider.contract.MarketDataProvider#providerCode()}. */
    @NotBlank
    @Size(max = 50)
    @Column(name = "provider_code", length = 50, nullable = false, updatable = false)
    private String providerCode;

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
}

