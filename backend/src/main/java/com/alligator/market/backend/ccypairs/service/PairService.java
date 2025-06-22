package com.alligator.market.backend.ccypairs.service;

import com.alligator.market.backend.ccypairs.dto.PairCreateDto;
import com.alligator.market.backend.ccypairs.dto.PairDto;
import com.alligator.market.backend.ccypairs.dto.PairUpdateDto;

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
