package com.alligator.market.backend.provider.catalog.persistence.jpa;

import com.alligator.market.backend.common.persistence.jpa.entity.BaseEntity;
import com.alligator.market.backend.provider.catalog.persistence.jpa.descriptor.ProviderDescriptorEmbeddable;
import com.alligator.market.backend.provider.catalog.persistence.jpa.policy.ProviderPolicyEmbeddable;
import com.alligator.market.domain.provider.code.ProviderCode;
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
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Checks;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.NaturalId;

/**
 * JPA-сущность провайдера рыночных данных.
 *
 * <p>Ключевые моменты:</p>
 * <ol>
 *     <li>1) Таблица {@code market_data_provider} является статичным справочником, предназначенным
 *     исключительно для отображения информации о провайдерах.</li>
 *     <li>2) Процесс обновления данных выполняется доменным процессом {@link ProviderSynchronizer}
 *     напрямую в БД через SQL (стратегия {@code DELETE -> INSERT}). Это выносит управление жизненным циклом записей
 *     за пределы механизмов JPA (Hibernate не отслеживает состояния сущностей при обновлении, не требуются
 *     специальные конструкторы или сеттеры для синхронизации).</li>
 *     <li>3) Аннотации и инкапсуляция:
 *         <ul>
 *             <li>3.1) {@link Immutable} и отсутствие сеттеров отражают read-only статус сущности для Hibernate.</li>
 *             <li>3.2) {@link NoArgsConstructor} с {@code PROTECTED} и отсутствие публичных конструкторов исключают
 *             случайное создание или изменение сущности в обход процесса синхронизации.</li>
 *             <li>3.3) {@link Access} с {@code FIELD} обеспечивает прямой доступ Hibernate к полям, сохраняя
 *             строгую инкапсуляцию класса.</li>
 *         </ul>
 *     </li>
 * </ol>
 */
@Entity
@Table(
        name = "market_data_provider",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_provider_code", columnNames = "provider_code")
        }
)
@Checks({
        @Check(
                name = "chk_provider_min_update_interval",
                constraints = "min_update_interval_seconds >= 1"
        ),
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
public class ProviderEntity extends BaseEntity {

    /**
     * Суррогатный PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Технический код провайдера ({@link NaturalId} – натуральный ключ).
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
     * Иммутабельный дескриптор.
     *
     * <p>{@link Embedded}: указывает, что поле является встраиваемым компонентом, чьи поля маппируются в ту же таблицу,
     * что и основная сущность; {@link Valid}: активирует каскадную валидацию ограничений встраиваемого объекта при
     * валидации родительской сущности.</p>
     */
    @Embedded
    @NotNull
    @Valid
    private ProviderDescriptorEmbeddable descriptor;

    /**
     * Иммутабельный набор параметров политики провайдера.
     *
     * <p>{@link Embedded}: указывает, что поле является встраиваемым компонентом, чьи поля маппируются в ту же таблицу,
     * что и основная сущность; {@link Valid}: активирует каскадную валидацию ограничений встраиваемого объекта при
     * валидации родительской сущности.</p>
     */
    @Embedded
    @NotNull
    @Valid
    private ProviderPolicyEmbeddable policy;
}
