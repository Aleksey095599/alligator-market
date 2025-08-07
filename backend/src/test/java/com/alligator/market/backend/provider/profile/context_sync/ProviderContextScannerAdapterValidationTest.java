package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.domain.provider.context_sync.DuplicateProviderProfileInContextException;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.provider.model.InstrumentHandler;
import com.alligator.market.domain.provider.model.MarketDataProvider;
import com.alligator.market.domain.provider.profile.model.AccessMethod;
import com.alligator.market.domain.provider.profile.model.DeliveryMode;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

/** Тесты проверки уникальности профилей в ProviderContextScannerAdapter. */
class ProviderContextScannerAdapterValidationTest {

    @Disabled
    @Test
    void shouldThrowOnDuplicateCode() {
        MarketDataProvider p1 = provider("A", "Name1");
        MarketDataProvider p2 = provider("A", "Name2");
        ProviderContextScannerAdapter scanner = new ProviderContextScannerAdapter(List.of(p1, p2));
        assertThrows(DuplicateProviderProfileInContextException.class, scanner::getProviderProfiles);
    }

    @Disabled
    @Test
    void shouldThrowOnDuplicateName() {
        MarketDataProvider p1 = provider("A", "Same");
        MarketDataProvider p2 = provider("B", "Same");
        ProviderContextScannerAdapter scanner = new ProviderContextScannerAdapter(List.of(p1, p2));
        assertThrows(DuplicateProviderProfileInContextException.class, scanner::getProviderProfiles);
    }


    /**
     * Создает тестовый экземпляр провайдера рыночных данных с указанным кодом и именем.
     *
     * @param code код провайдера
     * @param name отображаемое имя провайдера
     * @return экземпляр MarketDataProvider
     */
    private static MarketDataProvider provider(String code, String name) {
        ProviderProfile profile = new ProviderProfile(
                code,
                name,
                Set.of(InstrumentType.CURRENCY),
                DeliveryMode.PULL,
                AccessMethod.API_POLL,
                false,
                1
        );
        return new MarketDataProvider() {

            @Override
            public ProviderProfile profile() {
                return profile;
            }

            @Override
            public Map<InstrumentType, InstrumentHandler> instrumentHandlers() {
                return Map.of();
            }

            @Override
            public Flux<QuoteTick> quote(Instrument instrument) {
                return Flux.empty();
            }
        };
    }
}
