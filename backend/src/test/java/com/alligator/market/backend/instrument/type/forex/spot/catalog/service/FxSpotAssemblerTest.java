package com.alligator.market.backend.instrument.type.forex.spot.catalog.service;

import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.FxSpotUpdateDto;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.ref.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import com.alligator.market.domain.instrument.type.forex.spot.model.ValueDateCode;
import com.alligator.market.domain.instrument.type.forex.spot.utility.FxSpotNaming;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Тесты сборщика FX_SPOT. */
class FxSpotAssemblerTest {

    @Test
    void toDomainByCodeParsesCodeWithPrefix() {
        // Готовим фикстуры и заглушки
        CurrencyRepository currencyRepository = mock(CurrencyRepository.class);
        FxSpotAssembler assembler = new FxSpotAssembler(currencyRepository);
        String instrumentCode = FxSpotNaming.fxSpotCode("EUR", "USD", ValueDateCode.TOD);
        FxSpotUpdateDto dto = new FxSpotUpdateDto(4);

        Currency eur = new Currency("EUR", "Euro", "European Union", 2);
        Currency usd = new Currency("USD", "US Dollar", "United States", 2);
        when(currencyRepository.findByCode("EUR")).thenReturn(Optional.of(eur));
        when(currencyRepository.findByCode("USD")).thenReturn(Optional.of(usd));

        // Проверяем формирование доменной модели
        FxSpot fxSpot = assembler.toDomainByCode(instrumentCode, dto);

        assertThat(fxSpot.base()).isEqualTo(eur);
        assertThat(fxSpot.quote()).isEqualTo(usd);
        assertThat(fxSpot.valueDateCode()).isEqualTo(ValueDateCode.TOD);
        assertThat(fxSpot.quoteDecimal()).isEqualTo(4);
    }
}
