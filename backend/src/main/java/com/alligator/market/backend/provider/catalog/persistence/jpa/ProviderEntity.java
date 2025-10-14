package com.alligator.market.backend.provider.catalog.persistence.jpa;

import com.alligator.market.backend.common.persistence.jpa.BaseEntity;
import com.alligator.market.backend.provider.catalog.persistence.jpa.descriptor.ProviderDescriptorEmbeddable;
import com.alligator.market.backend.provider.catalog.persistence.jpa.policy.ProviderPolicyEmbeddable;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.reconciliation.ProviderSynchronizer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

/**
 * JPA-сущность провайдера рыночных данных.
 * 1) Все поля данной JPA-сущности и встроенных JPA-сущностей не обновляемые: логика синхронизации с контекстом
 *    приложения использует стратегию DELETE → INSERT {@link ProviderSynchronizer}.
 * 2) Данная JPA-сущность и встроенные JPA-сущности содержат конструкторы, чтобы обеспечить fail-fast проверку
 *    инвариантов (non-null, not blank, допустимые диапазоны) и формирование полностью инициализированного,
 *    иммутабельного состояния без сеттеров.
 * 3) Данная JPA-сущность и встроенные JPA-сущности содержат фабрики, чтобы упростить и стандартизировать создание
 *    из доменных моделей, инкапсулировать маппинг и повторно использовать валидацию.
 */
@Entity
@Table(
        name = "market_data_provider",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_provider_code", columnNames = "provider_code")
        }
)
@org.hibernate.annotations.Check(
        constraints = "min_update_interval_seconds >= 1" // Проверка на уровне БД для дополнительной безопасности
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD) // Доступ только через поля (игнорирование геттеров/сеттеров)
public class ProviderEntity extends BaseEntity {

    /** Суррогатный PK. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /** Технический код провайдера {@link MarketDataProvider#providerCode()}. */
    @NotBlank
    @Size(max = 50)
    @NaturalId() // Полезно, так как по сути данное поле это «естественный» неизменяемый ключ
    @Column(name = "provider_code", length = 50, nullable = false, updatable = false)
    private String providerCode;

    /** Иммутабельный дескриптор. */
    @Embedded
    @NotNull
    private ProviderDescriptorEmbeddable descriptor;

    /** Иммутабельный набор параметров политики провайдера. */
    @Embedded
    @NotNull
    private ProviderPolicyEmbeddable policy;

    /** Конструктор. */
    ProviderEntity(
            String providerCode,
            ProviderDescriptorEmbeddable descriptor,
            ProviderPolicyEmbeddable policy
    ) {
        this.providerCode = Objects.requireNonNull(providerCode, "providerCode must not be null");
        if (this.providerCode.isBlank()) {
            throw new IllegalArgumentException("providerCode must not be blank");
        }
        this.descriptor = Objects.requireNonNull(descriptor, "descriptor must not be null");
        this.policy = Objects.requireNonNull(policy, "policy must not be null");
    }

    /** Фабрика для создания иммутабельной сущности провайдера. */
    public static ProviderEntity of(
            String providerCode,
            ProviderDescriptorEmbeddable descriptor,
            ProviderPolicyEmbeddable policy
    ) {
        return new ProviderEntity(providerCode, descriptor, policy);
    }
}
