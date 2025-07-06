package com.alligator.market.backend.instrument.forex.currency_pair.service;

import com.alligator.market.backend.instrument.forex.currency_pair.dto.PairCreateDto;
import com.alligator.market.backend.instrument.forex.currency_pair.dto.PairDto;
import com.alligator.market.backend.instrument.forex.currency_pair.dto.PairUpdateDto;

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
