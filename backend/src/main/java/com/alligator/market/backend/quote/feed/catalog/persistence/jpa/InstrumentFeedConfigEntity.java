package com.alligator.market.backend.quote.feed.catalog.persistence.jpa;

import com.alligator.market.backend.common.persistence.jpa.entity.BaseEntity;
import com.alligator.market.backend.instrument.base.persistence.jpa.InstrumentBaseEntity;
import com.alligator.market.domain.quote.feed.InstrumentFeedRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.Locale;
import java.util.Objects;

/**
 * JPA-сущность конфигурации источников рыночных данных для финансового инструмента.
 */
@Entity
@Table(
        name = "instrument_feed_config",
        uniqueConstraints = {
                // Не может быть для одного инструмента источников с одинаковой ролью
                @UniqueConstraint(name = "uq_instrument_feed_role", columnNames = {"instrument_id", "feed_role"}),
                // Не может быть для одного инструмента источников с одинаковым кодом провайдера
                @UniqueConstraint(name = "uq_instrument_provider_code", columnNames = {"instrument_id", "provider_code"})
        },
        indexes = {
                // TODO
        }
)
@Check(
        // Ограничение CHECK (DDL): при включённой генерации схемы Hibernate создаст его;
        // при отключённой — служит «живой спецификацией» для миграций.
        // TODO
)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // <-- JPA-конструктор только для ORM в этом пакете и у наследников
public class InstrumentFeedConfigEntity extends BaseEntity {

    /**
     * Суррогатный PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Инструмент, для которого задаётся источник котировок.
     */
    @Setter(AccessLevel.NONE) // <-- Поле нельзя переназначать сеттером, задаётся один раз через конструктор
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instrument_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_instrument"), updatable = false, nullable = false)
    private InstrumentBaseEntity instrument;

    /**
     * Технический код провайдера (источника рыночных данных).
     */
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Z0-9_.-]+$") // <-- Ранняя валидация до БД (только буквы A - Z, цифры и знаки "_ . -")
    @Column(name = "provider_code", length = 50, nullable = false, updatable = false)
    private String providerCode;

    /**
     * Роль источника котировок (feed) для финансового инструмента.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "feed_role", length = 16, nullable = false, updatable = false)
    private InstrumentFeedRole feedRole;

    /**
     * Признак: запускать поток котировок или нет.
     */
    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    /**
     * Специальный конструктор — единственный безопасный способ создать сущность.
     */
    public InstrumentFeedConfigEntity(
            InstrumentBaseEntity instrument,
            String providerCode,
            InstrumentFeedRole feedRole,
            Boolean enabled
    ) {
        Objects.requireNonNull(instrument, "instrument must not be null");
        Objects.requireNonNull(providerCode, "providerCode must not be null");
        Objects.requireNonNull(feedRole, "feedRole must not be null");
        Objects.requireNonNull(enabled, "enabled must not be null");
        if (providerCode.isBlank()) {
            throw new IllegalArgumentException("providerCode must not be blank");
        }

        this.instrument = instrument;
        this.providerCode = providerCode;
        this.feedRole = feedRole;
        this.enabled = enabled;
    }

    /**
     * Меняет код провайдера.
     */
    public void updateProviderCode(String providerCode) {
        this.providerCode = normalizeProviderCode(providerCode);
    }

    /**
     * Включает/выключает поток котировок.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Нормализует код провайдера.
     */
    private static String normalizeProviderCode(String providerCode) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");

        String nCode = providerCode.strip();
        if (nCode.isEmpty()) {
            throw new IllegalArgumentException("providerCode must not be blank");
        }

        // Нормализуем в UPPERCASE
        return nCode.toUpperCase(Locale.ROOT);
    }
}
