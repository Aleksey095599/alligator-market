package com.alligator.market.backend.instrument.asset.forex.fxspot.catalog.persistence.jpa;

import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA-репозиторий инструментов FOREX_SPOT.
 */
public interface FxSpotJpaRepository extends JpaRepository<FxSpotEntity, Long> {

    /**
     * Найти сущность инструмента по коду.
     */
    Optional<FxSpotEntity> findByCode(String instrumentCode);

    /**
     * Проверить, используется ли заданная валюта хотя бы в одном инструменте.
     */
    boolean existsByBaseCurrency_CodeOrQuoteCurrency_Code(CurrencyCode baseCurrency, CurrencyCode quoteCurrency);
}
