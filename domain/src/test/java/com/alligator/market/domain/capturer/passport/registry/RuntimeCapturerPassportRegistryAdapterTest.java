package com.alligator.market.domain.capturer.passport.registry;

import com.alligator.market.domain.capturer.MarketDataCapturer;
import com.alligator.market.domain.capturer.passport.CapturerPassport;
import com.alligator.market.domain.capturer.policy.CapturerPolicy;
import com.alligator.market.domain.capturer.registry.RuntimeCapturerRegistry;
import com.alligator.market.domain.capturer.registry.SnapshotRuntimeCapturerRegistry;
import com.alligator.market.domain.capturer.vo.CapturerCode;
import com.alligator.market.domain.capturer.vo.CapturerDisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RuntimeCapturerPassportRegistryAdapterTest {

    @Test
    void exposesPassportsFromRuntimeCapturers() {
        CapturerCode code = CapturerCode.of("TEST_CAPTURER");
        CapturerPassport passport = passport("Test Capturer");
        RuntimeCapturerRegistry capturerRegistry = new SnapshotRuntimeCapturerRegistry(List.of(
                new TestMarketDataCapturer(code, passport)
        ));
        RuntimeCapturerPassportRegistry passportRegistry =
                new RuntimeCapturerPassportRegistryAdapter(capturerRegistry);

        assertEquals(Map.of(code, passport), passportRegistry.passportsByCode());
    }

    @Test
    void rejectsDuplicatePassportDisplayNames() {
        RuntimeCapturerRegistry capturerRegistry = new SnapshotRuntimeCapturerRegistry(List.of(
                new TestMarketDataCapturer(CapturerCode.of("FIRST_CAPTURER"), passport("Duplicate")),
                new TestMarketDataCapturer(CapturerCode.of("SECOND_CAPTURER"), passport("duplicate"))
        ));
        RuntimeCapturerPassportRegistry passportRegistry =
                new RuntimeCapturerPassportRegistryAdapter(capturerRegistry);

        assertThrows(IllegalArgumentException.class, passportRegistry::passportsByCode);
    }

    private static CapturerPassport passport(String displayName) {
        return new CapturerPassport(CapturerDisplayName.of(displayName));
    }

    private record TestMarketDataCapturer(
            CapturerCode capturerCode,
            CapturerPassport passport
    ) implements MarketDataCapturer {
        @Override
        public CapturerPolicy policy() {
            return new CapturerPolicy(Duration.ofSeconds(1));
        }
    }
}
