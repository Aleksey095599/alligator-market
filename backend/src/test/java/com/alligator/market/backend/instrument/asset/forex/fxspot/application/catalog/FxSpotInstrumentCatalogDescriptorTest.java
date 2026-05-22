package com.alligator.market.backend.instrument.asset.forex.fxspot.application.catalog;

import com.alligator.market.backend.instrument.application.catalog.model.InstrumentAttribute;
import com.alligator.market.backend.instrument.application.catalog.model.InstrumentCatalogItem;
import com.alligator.market.domain.instrument.Asset;
import com.alligator.market.domain.instrument.Product;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpotTenor;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FxSpotInstrumentCatalogDescriptorTest {

    private final FxSpotInstrumentCatalogDescriptor descriptor = new FxSpotInstrumentCatalogDescriptor();

    @Test
    void shouldDescribeFxSpotForInstrumentCatalog() {
        FxSpot fxSpot = new FxSpot(
                new Currency(CurrencyCode.of("eur"), "Euro", "Eurozone", 2),
                new Currency(CurrencyCode.of("usd"), "US Dollar", "United States", 2),
                FxSpotTenor.SPOT,
                5
        );

        InstrumentCatalogItem item = descriptor.describe(fxSpot);

        assertEquals(fxSpot.instrumentCode(), item.instrumentCode());
        assertEquals(fxSpot.instrumentSymbol().value(), item.displayName());
        assertEquals(Asset.FOREX, item.asset());
        assertEquals(Product.SPOT, item.product());
        assertEquals("FX SPOT currency pair", item.description());
        assertEquals(List.of(
                new InstrumentAttribute("baseCurrency", "Base currency", "EUR"),
                new InstrumentAttribute("quoteCurrency", "Quote currency", "USD"),
                new InstrumentAttribute("tenor", "Tenor", "SPOT"),
                new InstrumentAttribute(
                        "defaultQuoteFractionDigits",
                        "Default quote fraction digits",
                        "5"
                )
        ), item.attributes());
    }
}
