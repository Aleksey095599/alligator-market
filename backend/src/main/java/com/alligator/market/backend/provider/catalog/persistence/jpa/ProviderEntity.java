package com.alligator.market.backend.provider.catalog.persistence.jpa;

import com.alligator.market.backend.common.persistence.jpa.entity.BaseEntity;
import com.alligator.market.backend.provider.catalog.persistence.jpa.descriptor.ProviderDescriptorEmbeddable;
import com.alligator.market.backend.provider.catalog.persistence.jpa.policy.ProviderPolicyEmbeddable;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.reconciliation.ProviderSynchronizer;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import java.util.Objects;

/**
 * JPA-сущность провайдера рыночных данных.
 * <p>
 * 1) По бизнес логике таблица статическая и служит только для вывода информации о провайдерах во frontend.
 * 2) Все поля данной JPA-сущности и встроенных JPA-сущностей не обновляемые: логика синхронизации с контекстом
 * приложения использует стратегию DELETE --> INSERT {@link ProviderSynchronizer}.
 * 3) Данная JPA-сущность и встроенные JPA-сущности содержат конструкторы, чтобы обеспечить fail-fast проверку
 * инвариантов (non-null, not blank, допустимые диапазоны) и формирование полностью инициализированного,
 * иммутабельного состояния без сеттеров.
 * 4) Данная JPA-сущность и встроенные JPA-сущности содержат фабрики, чтобы упростить и стандартизировать создание
 * из доменных моделей, инкапсулировать маппинг и повторно использовать валидацию.
 */
@Entity
@Table(
        name = "market_data_provider",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_provider_code", columnNames = "provider_code")
        }
)
@org.hibernate.annotations.Check(
        // Ограничение CHECK (DDL): при включённой генерации схемы Hibernate создаст его;
        // при отключённой — служит «живой спецификацией» для миграций.
        constraints = "min_update_interval_seconds >= 1 " +
                "AND provider_code = upper(provider_code) " +
                "AND provider_code ~ '^[A-Z0-9_.-]+$' " +
                "AND delivery_mode IN ('PULL', 'PUSH') " +
                "AND access_method IN ('API_POLL', 'WEBSOCKET', 'FIX_PROTOCOL')"
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // <-- JPA-конструктор только для ORM в этом пакете и у наследников
@Access(AccessType.FIELD) // <-- Маппинг по полям: при чтении/записи ORM не вызывает геттеры/сеттеры
@Immutable // <-- Делает сущность read-only для ORM: Hibernate не генерирует UPDATE; изменения игнорируются на flush
@Cacheable // <-- Подключаем 2-й уровень кэша (если включен в конфигурации)
@org.hibernate.annotations.Cache( // <-- Стратегия 2L-кэша: только чтение; отдельный регион
        usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY,
        region = "provider"
)
@NaturalIdCache // <-- Кеширование обращений по натуральному ключу (provider_code)
public class ProviderEntity extends BaseEntity {

    /**
     * Суррогатный PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Технический код провайдера {@link MarketDataProvider#providerCode()}.
     */
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Z0-9_.-]+$") // <-- Ранняя валидация до БД (только буквы A - Z, цифры и знаки "_ . -")
    @NaturalId() // <-- Помечаем поле как натуральный ключ
    @Column(name = "provider_code", length = 50, nullable = false, updatable = false)
    private String providerCode;

    /**
     * Иммутабельный дескриптор.
     */
    @Embedded
    @NotNull
    @Valid // <-- Каскадная валидация вложенного embeddable
    private ProviderDescriptorEmbeddable descriptor;

    /**
     * Иммутабельный набор параметров "политики провайдера".
     */
    @Embedded
    @NotNull
    @Valid // <-- Каскадная валидация вложенного embeddable
    private ProviderPolicyEmbeddable policy;

    /**
     * Специальный конструктор — единственный безопасный способ создать сущность.
     *
     * @param providerCode Технический код провайдера
     * @param descriptor   Иммутабельный дескриптор
     * @param policy       Иммутабельный набор параметров "политики провайдера"
     */
    ProviderEntity(
            String providerCode,
            ProviderDescriptorEmbeddable descriptor,
            ProviderPolicyEmbeddable policy
    ) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(descriptor, "descriptor must not be null");
        Objects.requireNonNull(policy, "policy must not be null");
        if (providerCode.isBlank()) {
            throw new IllegalArgumentException("providerCode must not be blank");
        }

        this.providerCode = providerCode;
        this.descriptor = descriptor;
        this.policy = policy;
    }

    /**
     * Фабрика для создания иммутабельной сущности провайдера.
     */
    @SuppressWarnings("unused")
    public static ProviderEntity of(
            String providerCode,
            ProviderDescriptorEmbeddable descriptor,
            ProviderPolicyEmbeddable policy
    ) {
        return new ProviderEntity(providerCode, descriptor, policy);
    }
}
