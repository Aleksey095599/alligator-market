package com.alligator.market.domain.instrument.asset.forex.fxspot.repository;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;

import java.util.List;
import java.util.Optional;

public interface FxSpotRepository {

    FxSpot create(FxSpot fxSpot);

    FxSpot update(FxSpot fxSpot);

    void deleteByCode(InstrumentCode instrumentCode);

    Optional<FxSpot> findByCode(InstrumentCode instrumentCode);

    List<FxSpot> findAll();
}
