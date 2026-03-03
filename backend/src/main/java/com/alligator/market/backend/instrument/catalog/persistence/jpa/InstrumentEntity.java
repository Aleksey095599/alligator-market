package com.alligator.market.backend.instrument.catalog.persistence.jpa;

import com.alligator.market.backend.common.persistence.jpa.entity.BaseEntity;
import com.alligator.market.backend.instrument.catalog.persistence.jpa.converter.InstrumentCodeConverter;
import com.alligator.market.backend.instrument.catalog.persistence.jpa.converter.InstrumentSymbolConverter;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.type.AssetClass;
import com.alligator.market.domain.instrument.type.ContractType;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
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
 */
@Entity
@Table(
        name = "instrument",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_instrument_code", columnNames = "code")
        },
        indexes = {
                @Index(name = "idx_instrument_asset_class", columnList = "asset_class"),
                @Index(name = "idx_instrument_contract_type", columnList = "contract_type")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class InstrumentEntity extends BaseEntity {

    /**
     * Суррогатный PK.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Внутренний код инструмента (идентификатор).
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @NaturalId
    @Convert(converter = InstrumentCodeConverter.class)
    @Column(name = "code", length = 50, nullable = false, updatable = false)
    private InstrumentCode code;

    /**
     * Символ инструмента для отображения в UI.
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @Convert(converter = InstrumentSymbolConverter.class)
    @Column(name = "symbol", length = 50, nullable = false, updatable = false)
    private InstrumentSymbol symbol;

    /**
     * Класс актива инструмента.
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "asset_class", length = 32, nullable = false, updatable = false)
    private AssetClass assetClass;

    /**
     * Тип контракта инструмента.
     */
    @Setter(AccessLevel.NONE)
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "contract_type", length = 32, nullable = false, updatable = false)
    private ContractType contractType;

    /**
     * Метод однократной инициализации полей родительской сущности.
     */
    protected final void initIdentity(
            InstrumentCode instrumentCode,
            InstrumentSymbol instrumentSymbol,
            AssetClass assetClass,
            ContractType contractType
    ) {
        if (this.code != null || this.symbol != null || this.assetClass != null || this.contractType != null) {
            throw new IllegalStateException("Instrument identity already initialized");
        }

        Objects.requireNonNull(instrumentCode, "instrumentCode must not be null");
        Objects.requireNonNull(instrumentSymbol, "instrumentSymbol must not be null");
        Objects.requireNonNull(assetClass, "assetClass must not be null");
        Objects.requireNonNull(contractType, "contractType must not be null");

        this.code = instrumentCode;
        this.symbol = instrumentSymbol;
        this.assetClass = assetClass;
        this.contractType = contractType;
    }
}
