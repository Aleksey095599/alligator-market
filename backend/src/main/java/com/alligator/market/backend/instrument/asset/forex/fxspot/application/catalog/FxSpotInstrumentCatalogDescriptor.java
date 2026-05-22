package com.alligator.market.backend.instrument.asset.forex.fxspot.application.catalog;

import com.alligator.market.backend.instrument.application.catalog.descriptor.InstrumentCatalogDescriptor;
import com.alligator.market.backend.instrument.application.catalog.model.InstrumentAttribute;
import com.alligator.market.backend.instrument.application.catalog.model.InstrumentCatalogItem;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;

import java.util.List;
import java.util.Objects;

public final class FxSpotInstrumentCatalogDescriptor implements InstrumentCatalogDescriptor {
    private static final String DESCRIPTION = "FX spot currency pair";

    @Override
    public boolean supports(Instrument instrument) {
        return instrument instanceof FxSpot;
    }

    @Override
    public InstrumentCatalogItem describe(Instrument instrument) {
        if (!(instrument instanceof FxSpot fxSpot)) {
            throw new IllegalArgumentException("Unsupported instrument type for FX spot catalog descriptor");
        }

        return describe(fxSpot);
    }

    public InstrumentCatalogItem describe(FxSpot fxSpot) {
        Objects.requireNonNull(fxSpot, "fxSpot must not be null");

        return new InstrumentCatalogItem(
                fxSpot.instrumentCode(),
                fxSpot.instrumentSymbol().value(),
                fxSpot.asset(),
                fxSpot.product(),
                DESCRIPTION,
                List.of(
                        new InstrumentAttribute(
                                "baseCurrency",
                                "Base currency",
                                fxSpot.base().code().value()
                        ),
                        new InstrumentAttribute(
                                "quoteCurrency",
                                "Quote currency",
                                fxSpot.quote().code().value()
                        ),
                        new InstrumentAttribute(
                                "tenor",
                                "Tenor",
                                fxSpot.tenor().code()
                        ),
                        new InstrumentAttribute(
                                "defaultQuoteFractionDigits",
                                "Default quote fraction digits",
                                Integer.toString(fxSpot.defaultQuoteFractionDigits())
                        )
                )
        );
    }
}
