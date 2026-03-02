package com.alligator.market.backend.instrument.base.persistence.jpa;

import com.alligator.market.backend.common.persistence.jpa.entity.BaseEntity;
import com.alligator.market.domain.instrument.base.Instrument;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
import com.alligator.market.domain.instrument.type.InstrumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

/**
 * Абстрактная родительская (базовая) JPA-сущность финансового инструмента.
 *
 * <p>Назначение: хранение и представление в базе данных общих атрибутов финансовых инструментов; все финансовые
 * поля сущности соответствуют доменному контракту {@link Instrument}.</p>
 *
 * <p>Пояснение некоторых аннотаций:</p>
 * <ul>
 *     <li>{@link Inheritance}: используется стратегия {@code JOINED}, при которой общие для всех финансовых
 *     инструментов поля хранятся в родительской таблице, а специфичные – в таблицах наследников.</li>
 *     <li>{@link NoArgsConstructor} с {@code PROTECTED}: конструктор без аргументов нужен только для ORM;
 *     вручную создаются только сущности-наследники, которые вызывают метод однократной инициализации
 *     полей родительской сущности.</li>
 * </ul>
 */
@Entity
@Table(
        name = "instrument",
        // Уникальность натурального ключа:
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_instrument_code", columnNames = "code")
        },
        // Индекс по типу инструмента полезен для быстрого поиска:
        indexes = {
                @Index(name = "idx_instrument_type", columnList = "type")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class InstrumentBaseEntity extends BaseEntity {

    /**
     * Суррогатный PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Внутренний код инструмента (уникален в контексте приложения).
     *
     * <p>Поле является натуральным ключом, поэтому {@code updatable=false} и
     * запрет на переназначение через сеттер {@code @Setter(AccessLevel.NONE)}.</p>
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @NaturalId()
    @Convert(converter = InstrumentCodeConverter.class)
    @Column(
            name = "code", length = 50,
            nullable = false,
            updatable = false
    )
    private InstrumentCode code;

    /**
     * Символ инструмента для отображения в UI.
     *
     * <p>Поле задает неизменяемый атрибут инструмента, поэтому {@code updatable=false} и
     * запрет на переназначение через сеттер {@code @Setter(AccessLevel.NONE)}.</p>
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @Convert(converter = InstrumentSymbolConverter.class)
    @Column(
            name = "symbol", length = 50,
            nullable = false,
            updatable = false
    )
    private InstrumentSymbol symbol;

    /**
     * Тип финансового инструмента.
     *
     * <p>Поле задает неизменяемый атрибут инструмента, поэтому {@code updatable=false} и
     * запрет на переназначение через сеттер {@code @Setter(AccessLevel.NONE)}.</p>
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(
            name = "type", length = 32,
            nullable = false,
            updatable = false
    )
    private InstrumentType type;

    /**
     * Метод однократной инициализации полей родительской сущности.
     *
     * <p>Вызывается из конструктора сущности-наследника и заполняет поля
     * {@code code}, {@code symbol}, {@code type}.</p>
     */
    // Пока не появились иные инструменты кроме FX_SPOT, давим предупреждение типа "SameParameterValue"
    @SuppressWarnings("SameParameterValue")
    protected final void initIdentity(
            InstrumentCode instrumentCode,
            InstrumentSymbol instrumentSymbol,
            InstrumentType type
    ) {
        // Защита от повторной инициализации
        if (this.code != null || this.symbol != null || this.type != null) {
            throw new IllegalStateException("Instrument identity already initialized");
        }

        // Базовые проверки аргументов
        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(instrumentSymbol, "instrumentSymbol must not be null");
        Objects.requireNonNull(type, "type must not be null");

        this.code = instrumentCode;
        this.symbol = instrumentSymbol;
        this.type = type;
    }
}
