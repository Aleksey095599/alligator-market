package com.alligator.market.backend.currency_pairs.service;

import com.alligator.market.backend.currency_pairs.dto.PairCreateDto;
import com.alligator.market.backend.currency_pairs.dto.PairDto;
import com.alligator.market.backend.currency_pairs.dto.PairUpdateDto;

import java.util.List;

/**
 * Интерфейс сервиса для операций с валютными парами.
 */
public interface PairService {

    String createPair(PairCreateDto dto);

    void updatePair(String pair, PairUpdateDto dto);

    void deletePair(String pair);

    List<PairDto> findAll();

}
