package com.alligator.market.backend.ccypairs.repository;

import com.alligator.market.backend.ccypairs.entity.Pair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с таблицей валютных пар.
 */
public interface PairRepository extends JpaRepository<Pair, Long> {

    Optional<Pair> findByPair(String pair);

    /* Проверяет, существует ли любая валютная пара с переданным кодом в code1 или code2 */
    boolean existsByCode1_CodeOrCode2_Code(String code1, String code2);

}

