package com.alligator.market.backend.instrument.application.catalog;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.catalog.FxSpotInstrumentCatalogDescriptor;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpotTenor;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.registry.runtime.SnapshotRuntimeInstrumentRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InstrumentCatalogServiceTest {

    @Test
    void shouldBuildCatalogItemsFromRuntimeRegistry() {
        FxSpot fxSpot = new FxSpot(
                new Currency(CurrencyCode.of("eur"), "Euro", "Eurozone", 2),
                new Currency(CurrencyCode.of("usd"), "US Dollar", "United States", 2),
                FxSpotTenor.SPOT,
                5
        );

        InstrumentCatalogService service = new InstrumentCatalogService(
                new SnapshotRuntimeInstrumentRegistry(List.of(fxSpot)),
                List.of(new FxSpotInstrumentCatalogDescriptor())
        );

        var items = service.findAll();

        assertEquals(1, items.size());
        assertEquals(fxSpot.instrumentCode(), items.getFirst().instrumentCode());
        assertEquals(fxSpot.instrumentSymbol().value(), items.getFirst().displayName());
    }
}
