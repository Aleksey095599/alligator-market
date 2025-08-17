package com.alligator.market.backend.instrument.catalog.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA-репозиторий базовых финансовых инструментов.
 */
public interface InstrumentJpaRepository extends JpaRepository<InstrumentEntity, Long> {

    /** Найти инструмент по внутреннему коду. */
    Optional<InstrumentEntity> findByCode(String code);
}
