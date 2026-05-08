package com.alligator.market.domain.instrument;

import com.alligator.market.domain.instrument.classification.Asset;
import com.alligator.market.domain.instrument.classification.Product;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;

public interface Instrument {

    InstrumentCode instrumentCode();

    InstrumentSymbol instrumentSymbol();

    Asset asset();

    Product product();
}
