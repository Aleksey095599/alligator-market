package com.alligator.market.backend.provider.catalog.passport.persistence.jpa;

import com.alligator.market.backend.common.persistence.jpa.entity.BaseEntity;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.model.passport.AccessMethod;
import com.alligator.market.domain.provider.model.passport.DeliveryMode;
import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.reconciliation.db.dao.ProviderPassportSyncDao;
import com.alligator.market.domain.provider.reconciliation.sync.service.ProviderPassportSynchronizer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
 * <p><b>Ключевые особенности</b></p>
 * <ul>
 *     <li>Поля сущности соответствуют доменной модели {@link ProviderPassport}; дополнительно добавлен
 *     код провайдера как натуральный ключ.</li>
 *     <li>Таблица {@code provider_passport} — статичный справочник метаданных провайдеров; записи не создаются
 *     и не изменяются через JPA.</li>
 *     <li>Процесс обновления данных выполняется доменным процессом {@link ProviderPassportSynchronizer} напрямую в БД
 *     через DAO {@link ProviderPassportSyncDao}.</li>
 * </ul>
 *
 * <p><b>Пояснение некоторых аннотаций</b></p>
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
public class PassportEntity extends BaseEntity {

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
    @NotNull
    @Convert(converter = ProviderCodeConverter.class)
    @NaturalId()
    @Column(
            name = "provider_code", length = 50,
            nullable = false,
            updatable = false
    )
    private ProviderCode providerCode;

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
    @Column(
            name = "access_method", length = 20,
            nullable = false,
            updatable = false
    )
    private AccessMethod accessMethod;

    /**
     * Поддержка массовой подписки одним запросом.
     */
    @Column(
            name = "bulk_subscription",
            nullable = false,
            updatable = false
    )
    private boolean bulkSubscription;
}
