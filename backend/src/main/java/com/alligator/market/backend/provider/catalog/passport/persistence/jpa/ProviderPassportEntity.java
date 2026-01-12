package com.alligator.market.backend.provider.catalog.passport.persistence.jpa;

import com.alligator.market.backend.common.persistence.jpa.entity.BaseEntity;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.contract.passport.AccessMethod;
import com.alligator.market.domain.provider.contract.passport.DeliveryMode;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import com.alligator.market.domain.provider.reconciliation.ProviderSynchronizer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Checks;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NaturalId;

/**
 * JPA-сущность паспорта провайдера рыночных данных.
 *
 * <p>Ключевые особенности:</p>
 * <ul>
 *     <li>1) Поля сущности соответсвюут доменной модели {@link ProviderPassport}; дополнительно добавлен
 *     код провадера как натуральный ключ.</li>
 *     <li>2) Таблица {@code provider_passport} — статичный справочник метаданных провайдеров; записи не создаются
 *     и не изменяются через JPA.</li>
 *     <li>3) Процесс обновления данных выполняется доменным процессом {@link ProviderSynchronizer} напрямую в БД
 *     через SQL по стратегии {@code DELETE -> INSERT}; жизненный цикл записей управляется вне Hibernate.</li>
 * </ul>
 *
 * <p>Пояснение некоторых аннотаций:</p>
 * <ul>
 *     <li>{@link Immutable} и отсутствие сеттеров отражают read-only статус сущности.</li>
 *     <li>{@link NoArgsConstructor} с {@code PROTECTED} и отсутствие публичных конструкторов исключают
 *     случайное создание или изменение сущности в обход процесса синхронизации.</li>
 *     <li>{@link Access} с {@code FIELD} обеспечивает прямой доступ Hibernate к полям, сохраняя
 *     строгую инкапсуляцию класса и исключая необходимость публичного API для маппинга.</li>
 * </ul>
 */
@Entity
@Table(
        name = "provider_passport",
        // Уникальность кода провайдера (натуральный ключ)
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_provider_code", columnNames = "provider_code")
        }
)
@Checks({
        @Check(
                name = "chk_provider_code_pattern",
                constraints = "provider_code ~ '" + ProviderCode.PATTERN + "'"
        ),
        @Check(
                name = "chk_provider_delivery_mode_allowed",
                constraints = "delivery_mode IN ('PULL', 'PUSH')"
        ),
        @Check(
                name = "chk_provider_access_method_allowed",
                constraints = "access_method IN ('API_POLL', 'WEBSOCKET', 'FIX_PROTOCOL')"
        )
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Access(AccessType.FIELD)
@Immutable
public class ProviderPassportEntity extends BaseEntity {

    /**
     * Суррогатный PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Технический код провайдера (натуральный ключ).
     */
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = ProviderCode.PATTERN)
    @NaturalId()
    @Column(
            name = "provider_code", length = 50,
            nullable = false,
            updatable = false
    )
    private String providerCode;

    /**
     * Имя провайдера (user friendly).
     */
    @NotBlank
    @Size(max = 50)
    @Column(
            name = "display_name", length = 50,
            nullable = false,
            updatable = false
    )
    private String displayName;

    /**
     * Режим доставки рыночных данных.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(
            name = "delivery_mode", length = 10,
            nullable = false,
            updatable = false
    )
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
