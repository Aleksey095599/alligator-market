package com.alligator.market.backend.fx.pairs.service;

import com.alligator.market.backend.fx.pairs.dto.PairDto;
import com.alligator.market.backend.fx.pairs.dto.PairUpdateDto;

import java.util.List;

/* Интерфейс сервиса для операций с валютными парами. */
public interface PairService {

    String createPair(PairDto dto);

    void updatePair(String pair, PairUpdateDto dto);

    void deletePair(String pair);

    List<PairDto> findAll();
}
