package com.alligator.market.domain.instrument.registry.runtime;

import com.alligator.market.domain.instrument.Asset;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.Product;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.vo.InstrumentSymbol;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SnapshotRuntimeInstrumentRegistryTest {

    @Test
    void findsInstrumentByCode() {
        Instrument firstInstrument = instrument("FOREX_SPOT_CNYRUB_TOM");
        Instrument secondInstrument = instrument("FOREX_SPOT_USDRUB_TOM");
        RuntimeInstrumentRegistry registry = new SnapshotRuntimeInstrumentRegistry(List.of(
                firstInstrument,
                secondInstrument
        ));

        assertEquals(
                firstInstrument,
                registry.findByCode(firstInstrument.instrumentCode()).orElseThrow()
        );
        assertEquals(
                secondInstrument,
                registry.instrumentsByCode().get(secondInstrument.instrumentCode())
        );
    }

    @Test
    void returnsEmptyWhenInstrumentCodeIsUnknown() {
        RuntimeInstrumentRegistry registry = new SnapshotRuntimeInstrumentRegistry(List.of(
                instrument("FOREX_SPOT_CNYRUB_TOM")
        ));

        assertTrue(registry.findByCode(InstrumentCode.of("FOREX_SPOT_USDRUB_TOM")).isEmpty());
    }

    @Test
    void rejectsDuplicateInstrumentCodes() {
        InstrumentCode code = InstrumentCode.of("FOREX_SPOT_CNYRUB_TOM");

        assertThrows(
                IllegalArgumentException.class,
                () -> new SnapshotRuntimeInstrumentRegistry(List.of(
                        new TestInstrument(code),
                        new TestInstrument(code)
                ))
        );
    }

    private static Instrument instrument(String code) {
        return new TestInstrument(InstrumentCode.of(code));
    }

    private record TestInstrument(InstrumentCode instrumentCode) implements Instrument {

        @Override
        public InstrumentSymbol instrumentSymbol() {
            return InstrumentSymbol.of(instrumentCode.value());
        }

        @Override
        public Asset asset() {
            return Asset.FOREX;
        }

        @Override
        public Product product() {
            return Product.SPOT;
        }
    }
}
