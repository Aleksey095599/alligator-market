package com.alligator.market.backend.instrument.type.forex.spot.catalog.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA-репозиторий инструментов FX_SPOT.
 */
public interface FxSpotJpaRepository extends JpaRepository<FxSpotEntity, Long> {

    /** Сохранить инструмент. */
    @Override
    @NonNull
    <S extends FxSpotEntity> S save(@NonNull S entity);

    /** Удалить инструмент по коду. */
    void deleteByCode(String code);

    /** Найти инструмент по коду. */
    Optional<FxSpotEntity> findByCode(String code);

    /** Вернуть все инструменты. */
    List<FxSpotEntity> findAllByOrderByCodeAsc();

    /** Проверить, используется ли заданная валюта хотя бы в одном инструменте. */
    boolean existsByBaseCurrency_CodeOrQuoteCurrency_Code(String baseCurrency, String quoteCurrency);
}
