package com.alligator.market.backend.fx.pairs.repository;

import com.alligator.market.backend.fx.pairs.entity.Pair;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/* Репозиторий для работы с таблицей ccypair. */
public interface PairRepository extends JpaRepository<Pair, Long> {

    Optional<Pair> findByPair(String pair);
}

