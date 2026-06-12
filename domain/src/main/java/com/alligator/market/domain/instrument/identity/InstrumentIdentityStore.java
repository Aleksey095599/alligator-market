package com.alligator.market.domain.instrument.identity;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;

public interface InstrumentIdentityStore {

    boolean contains(InstrumentCode code);

    List<InstrumentCode> instrumentCodes();
}
