package com.alligator.market.domain.instrument.asset.forex.fxspot.repository;

import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.repository.InstrumentRepository;

public interface FxSpotRepository extends InstrumentRepository<FxSpot> {

    FxSpot create(FxSpot fxSpot);

    FxSpot update(FxSpot fxSpot);

    void deleteByCode(InstrumentCode instrumentCode);
}
