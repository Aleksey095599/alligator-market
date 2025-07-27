package com.alligator.market.backend.provider.profile.context_sync;

import com.alligator.market.backend.provider.profile.exception.DuplicateProviderProfileException;
import com.alligator.market.domain.instrument.Instrument;
import com.alligator.market.domain.instrument.InstrumentType;
import com.alligator.market.domain.provider.MarketDataProvider;
import com.alligator.market.domain.provider.profile.AccessMethod;
import com.alligator.market.domain.provider.profile.DeliveryMode;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

/** Тесты проверки уникальности профилей в ProviderContextScanner. */
class ProviderContextScannerValidationTest {

    @Disabled
    @Test
    void shouldThrowOnDuplicateCode() {
        MarketDataProvider p1 = provider("A", "Name1");
        MarketDataProvider p2 = provider("A", "Name2");
        ProviderContextScanner scanner = new ProviderContextScanner(List.of(p1, p2));
        assertThrows(DuplicateProviderProfileException.class, scanner::getProviderProfiles);
    }

    @Disabled
    @Test
    void shouldThrowOnDuplicateName() {
        MarketDataProvider p1 = provider("A", "Same");
        MarketDataProvider p2 = provider("B", "Same");
        ProviderContextScanner scanner = new ProviderContextScanner(List.of(p1, p2));
        assertThrows(DuplicateProviderProfileException.class, scanner::getProviderProfiles);
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
            public Flux<QuoteTick> streamQuotes(Instrument instrument) {
                return Flux.empty();
            }
        };
    }
}
