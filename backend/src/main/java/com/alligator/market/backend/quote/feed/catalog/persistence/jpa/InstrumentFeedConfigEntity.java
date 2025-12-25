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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.util.Locale;
import java.util.Objects;

/**
 * JPA-сущность конфигурации источников котировок (feed) для финансового инструмента.
 *
 * <p>Основная задача таблицы {@code instrument_feed_config} – задать соответствие: финансовый инструмент ↔ провайдер рыночных данных.</p>
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
        // CHECK: при DDL-генерации создаётся Hibernate; иначе — «живая» спецификация для миграций.
        constraints =
                "provider_code = upper(provider_code) " +
                        "AND provider_code ~ '^[A-Z0-9_.-]+$' " +
                        "AND feed_role IN ('PRIMARY','SECONDARY')"
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// <-- Нельзя создавать вручную через new Entity(): конструктор без аргументов нужен только ORM
@Access(AccessType.FIELD) // <-- Маппинг по полям: при чтении/записи ORM не вызывает геттеры/сеттеры
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
    @JoinColumn(
            name = "instrument_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_ifc_instrument"),
            nullable = false, updatable = false)
    private InstrumentBaseEntity instrument;

    /**
     * Технический код провайдера (источника рыночных данных).
     *
     * <p>Soft reference: без FK на market_data_provider, БД не гарантирует что такой провайдер существует.
     */
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Z0-9_.-]+$")
    @Column(name = "provider_code", length = 50, nullable = false)
    private String providerCode;

    /**
     * Роль источника котировок (feed) для финансового инструмента.
     */
    @Setter(AccessLevel.NONE) // <-- Поле нельзя переназначать сеттером, задаётся один раз через конструктор
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "feed_role", length = 16, nullable = false, updatable = false)
    private InstrumentFeedRole feedRole;

    /**
     * Признак: запускать поток котировок или нет.
     */
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    /**
     * Специальный конструктор — единственный безопасный способ создать сущность.
     */
    public InstrumentFeedConfigEntity(
            InstrumentBaseEntity instrument,
            String providerCode,
            InstrumentFeedRole feedRole,
            boolean enabled
    ) {
        this.instrument = Objects.requireNonNull(instrument, "instrument must not be null");
        this.feedRole = Objects.requireNonNull(feedRole, "feedRole must not be null");
        this.providerCode = normalizeProviderCode(providerCode);
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
     * Нормализует и валидирует код провайдера.
     */
    private static String normalizeProviderCode(String providerCode) {
        Objects.requireNonNull(providerCode, "providerCode must not be null");

        String n = providerCode.strip(); // <-- обрезаем пробелы
        if (n.isEmpty()) {
            throw new IllegalArgumentException("providerCode must not be blank");
        }

        // Нормализуем в UPPERCASE
        n = n.toUpperCase(Locale.ROOT);

        // Fail-fast проверка формата (дублирует @Pattern/@Check, но даёт раннюю ошибку).
        if (!n.matches("^[A-Z0-9_.-]+$")) {
            throw new IllegalArgumentException("providerCode has invalid format: " + n);
        }

        return n;
    }
}
