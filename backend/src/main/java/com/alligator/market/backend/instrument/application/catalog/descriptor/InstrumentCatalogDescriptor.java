package com.alligator.market.backend.instrument.application.catalog.descriptor;

import com.alligator.market.backend.instrument.application.catalog.model.InstrumentCatalogItem;
import com.alligator.market.domain.instrument.Instrument;

public interface InstrumentCatalogDescriptor {

    boolean supports(Instrument instrument);

    InstrumentCatalogItem describe(Instrument instrument);
}
