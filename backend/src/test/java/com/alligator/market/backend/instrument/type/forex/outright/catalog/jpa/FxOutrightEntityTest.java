package com.alligator.market.backend.instrument.type.forex.outright.catalog.jpa;

import com.alligator.market.backend.instrument.type.forex.outright.reference.currency.catalog.jpa.CurrencyEntity;
import com.alligator.market.domain.instrument.type.forex.outright.model.ValueDateCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Модульный тест для {@link FxOutrightEntity}.
 */
class FxOutrightEntityTest {

    @Test
    void shouldGenerateCodeLikeDomainModel() {
        CurrencyEntity base = new CurrencyEntity();
        base.setCode("EUR");
        CurrencyEntity quote = new CurrencyEntity();
        quote.setCode("USD");

        FxOutrightEntity entity = new FxOutrightEntity();
        entity.setBaseCurrency(base);
        entity.setQuoteCurrency(quote);
        entity.setValueDateCode(ValueDateCode.TOM);

        entity.__onGenerateCode();

        assertEquals("EURUSD_TOM", entity.getCode());
    }
}
