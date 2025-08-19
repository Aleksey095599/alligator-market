package com.alligator.market.backend.instrument.catalog.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA-репозиторий для работы с родительскими сущностями финансовых инструментов.
 */
public interface InstrumentJpaRepository extends JpaRepository<InstrumentEntity, Long> {

    /** Найти инструмент по внутреннему коду. */
    Optional<InstrumentEntity> findByCode(String code);
}
