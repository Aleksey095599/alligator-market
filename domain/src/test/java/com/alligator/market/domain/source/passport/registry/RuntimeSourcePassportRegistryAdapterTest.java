package com.alligator.market.domain.source.passport.registry;

import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.marketdata.tick.level.source.SourceTick;
import com.alligator.market.domain.source.MarketSource;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.registry.runtime.RuntimeSourcePassportRegistry;
import com.alligator.market.domain.source.passport.registry.runtime.RuntimeSourcePassportRegistryAdapter;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.registry.RuntimeSourceRegistry;
import com.alligator.market.domain.source.registry.SnapshotRuntimeSourceRegistry;
import com.alligator.market.domain.source.vo.SourceCode;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RuntimeSourcePassportRegistryAdapterTest {

    @Test
    void exposesPassportsFromRuntimeSources() {
        SourceCode code = SourceCode.of("TEST_SOURCE");
        SourcePassport passport = passport("Test Source");
        RuntimeSourceRegistry sourceRegistry = new SnapshotRuntimeSourceRegistry(List.of(
                new TestMarketSource(code, passport)
        ));
        RuntimeSourcePassportRegistry passportRegistry =
                new RuntimeSourcePassportRegistryAdapter(sourceRegistry);

        assertEquals(Map.of(code, passport), passportRegistry.passportsByCode());
    }

    @Test
    void rejectsDuplicatePassportDisplayNames() {
        RuntimeSourceRegistry sourceRegistry = new SnapshotRuntimeSourceRegistry(List.of(
                new TestMarketSource(SourceCode.of("FIRST_SOURCE"), passport("Duplicate")),
                new TestMarketSource(SourceCode.of("SECOND_SOURCE"), passport("duplicate"))
        ));
        RuntimeSourcePassportRegistry passportRegistry =
                new RuntimeSourcePassportRegistryAdapter(sourceRegistry);

        assertThrows(IllegalArgumentException.class, passportRegistry::passportsByCode);
    }

    private static SourcePassport passport(String displayName) {
        return new SourcePassport(SourceDisplayName.of(displayName));
    }

    private record TestMarketSource(
            SourceCode code,
            SourcePassport passport
    ) implements MarketSource {
        @Override
        public <I extends Instrument> Publisher<SourceTick> streamSourceTicks(I instrument) {
            throw new UnsupportedOperationException("Test source does not stream ticks");
        }
    }
}
