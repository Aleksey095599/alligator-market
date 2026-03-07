package com.alligator.market.backend.quote.feed.catalog.persistence.jpa;

import com.alligator.market.backend.common.persistence.jpa.entity.BaseEntity;
import com.alligator.market.backend.instrument.catalog.persistence.jpa.InstrumentEntity;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.quote.feed.InstrumentFeedRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.Objects;

/**
 * JPA-сущность конфигурации источников котировок для финансового инструмента.
 *
 * <p><b>Ключевые особенности</b></p>
 * <ul>
 *     <li>Таблица данной сущности задает соответствие "финансовый инструмент → провайдер рыночных данных".</li>
 * </ul>
 *
 * <p><b>Пояснение некоторых аннотаций</b></p>
 * <ul>
 *     <li>{@link NoArgsConstructor} с {@code PROTECTED}: конструктор без аргументов нужен только для ORM;
 *     вручную сущность создается через специализированный конструктор.</li>
 *     <li>Поля {@link InstrumentFeedConfigEntity#instrument} и {@link InstrumentFeedConfigEntity#feedRole}
 *     задают неизменяемые атрибуты, поэтому к ним отключен доступ через сеттер {@link Setter}.</li>
 * </ul>
 */
@Entity
@Table(
        name = "instrument_feed_config",
        uniqueConstraints = {
                // Не может быть для одного инструмента несколько источников с одинаковой ролью
                @UniqueConstraint(name = "uq_ifc_instrument_feed_role", columnNames = {"instrument_id", "feed_role"}),
                // Не может быть для одного инструмента несколько источников с одинаковым кодом провайдера
                @UniqueConstraint(name = "uq_ifc_instrument_provider_code", columnNames = {"instrument_id", "provider_code"})
        },
        indexes = {
                // Ускоряет массовый отбор включённых записей при старте/перезапуске потоков:
                @Index(name = "idx_ifc_enabled", columnList = "enabled")
        }
)
@Check(
        constraints =
                "provider_code = upper(provider_code) " +
                        "AND provider_code ~ '" + InstrumentFeedConfigEntity.PROVIDER_CODE_PATTERN + "' " +
                        "AND feed_role IN ('PRIMARY','SECONDARY')"
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InstrumentFeedConfigEntity extends BaseEntity {

    /* Шаблон кода провайдера. */
    static final String PROVIDER_CODE_PATTERN = ProviderCode.PATTERN;

    /**
     * Суррогатный PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Инструмент, для которого задаётся источник котировок.
     *
     * <p>Задается как внешний ключ на таблицу инструментов; нельзя обновлять – задается один раз при создании записи;
     * frontend должен предлагать список инструментов из таблицы инструментов {@link InstrumentEntity} при попытке
     * заполнения формы для передачи данных в backend.</p>
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "instrument_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_ifc_instrument_base"),
            nullable = false,
            updatable = false
    )
    private InstrumentEntity instrument;

    /**
     * Код провайдера.
     *
     * TODO: Подумать нельзя ли задать как обычную ссылку (жесткую), но допускающую null. Например, если будет удален
     * пасспрот провайдера, то в таблице значения где ссылались на него заменяться на null.
     *
     * <p>Soft reference – без внешнего ключа на таблицу provider_passport (БД не гарантирует существование провайдера);
     * можно обновлять; frontend должен предлагать текущий доступный список кодов провайдеров
     * из таблицы provider_passport при попытке заполнения формы для данного поля.</p>
     */
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = PROVIDER_CODE_PATTERN)
    @Column(
            name = "provider_code", length = 50,
            nullable = false
    )
    private String providerCode;

    /**
     * Роль источника котировок (feed) для финансового инструмента.
     *
     * <p>Нельзя обновлять – задается один раз при создании записи для большей строгости и чтобы избежать излишние
     * усложнения поведения при обновлении записи.</p>
     */
    @Setter(AccessLevel.NONE) // <-- Поле нельзя переназначать сеттером, задаётся один раз через конструктор
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(
            name = "feed_role", length = 16,
            nullable = false,
            updatable = false
    )
    private InstrumentFeedRole feedRole;

    /**
     * Признак: запускать поток котировок или нет.
     *
     * <p>Можно обновлять.</p>
     */
    @Column(
            name = "enabled",
            nullable = false
    )
    private boolean enabled;

    /**
     * Специальный конструктор – единственный безопасный способ создать сущность.
     */
    public InstrumentFeedConfigEntity(
            InstrumentEntity instrument,
            String providerCode,
            InstrumentFeedRole feedRole,
            boolean enabled
    ) {
        this.instrument = Objects.requireNonNull(instrument, "instrument must not be null");
        this.feedRole = Objects.requireNonNull(feedRole, "feedRole must not be null");
        this.providerCode = ProviderCode.of(providerCode).value();
        this.enabled = enabled;
    }

    /**
     * Меняет код провайдера.
     */
    public void updateProviderCode(String providerCode) {
        this.providerCode = ProviderCode.of(providerCode).value();
    }

    /**
     * Включает/выключает поток котировок.
     */
    public void updateEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
