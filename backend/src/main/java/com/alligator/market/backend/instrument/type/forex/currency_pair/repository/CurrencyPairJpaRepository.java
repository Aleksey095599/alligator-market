package com.alligator.market.backend.instrument.type.forex.currency_pair.repository;

import com.alligator.market.backend.instrument.type.forex.currency_pair.entity.CurrencyPairEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JPA-репозиторий для работы с сущностями {@link CurrencyPairEntity} в таблице <code>currency_pair</code>.
 */
public interface CurrencyPairJpaRepository extends JpaRepository<CurrencyPairEntity, Long> {

    Optional<CurrencyPairEntity> findByPairCode(String pairCode);

    /** Проверяет, существует ли любая валютная пара с переданным кодом в base или quote. */
    boolean existsByBase_CodeOrQuote_Code(String base, String quote);
}

