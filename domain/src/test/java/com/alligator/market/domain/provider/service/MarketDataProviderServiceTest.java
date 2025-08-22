package com.alligator.market.domain.provider.service;

import com.alligator.market.domain.instrument.contract.Instrument;
import com.alligator.market.domain.instrument.contract.InstrumentType;
import com.alligator.market.domain.provider.contract.InstrumentHandler;
import com.alligator.market.domain.provider.contract.MarketDataProvider;
import com.alligator.market.domain.provider.exception.ProviderHandlerMismatchException;
import com.alligator.market.domain.provider.exception.ProviderHandlersInvalidException;
import com.alligator.market.domain.provider.exception.ProviderInstrumentHandlerDuplicateException;
import com.alligator.market.domain.provider.profile.model.ProviderAccessMethod;
import com.alligator.market.domain.provider.profile.model.ProviderDeliveryMode;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.quote.QuoteTick;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/* Тесты для MarketDataProviderService. */
public class MarketDataProviderServiceTest {

    /* Тестируемый сервис. */
    private final MarketDataProviderService service = new MarketDataProviderService();

    /* Проверяем, что пустой набор обработчиков вызывает исключение. */
    @Test
    void validateHandlersShouldFailOnEmptyHandlers() {
        MarketDataProvider provider = new TestProvider("P1", Collections.emptySet());
        assertThrows(ProviderHandlersInvalidException.class, () -> service.validateHandlers(provider));
    }

    /* Проверяем, что обработчик другого провайдера вызывает исключение. */
    @Test
    void validateHandlersShouldFailOnMismatch() {
        Set<InstrumentHandler> handlers = Set.of(new TestHandler("P2", InstrumentType.FX_OUTRIGHT));
        MarketDataProvider provider = new TestProvider("P1", handlers);
        assertThrows(ProviderHandlerMismatchException.class, () -> service.validateHandlers(provider));
    }

    /* Проверяем, что дублирование типа инструмента вызывает исключение. */
    @Test
    void validateHandlersShouldFailOnDuplicateType() {
        Set<InstrumentHandler> handlers = new HashSet<>();
        handlers.add(new TestHandler("P1", InstrumentType.FX_OUTRIGHT));
        handlers.add(new TestHandler("P1", InstrumentType.FX_OUTRIGHT));
        MarketDataProvider provider = new TestProvider("P1", handlers);
        assertThrows(ProviderInstrumentHandlerDuplicateException.class, () -> service.validateHandlers(provider));
    }

    /* Проверяем, что корректные обработчики проходят проверку. */
    @Test
    void validateHandlersShouldPass() {
        Set<InstrumentHandler> handlers = Set.of(new TestHandler("P1", InstrumentType.FX_OUTRIGHT));
        MarketDataProvider provider = new TestProvider("P1", handlers);
        assertDoesNotThrow(() -> service.validateHandlers(provider));
    }

    /* Тестовая реализация провайдера. */
    private static class TestProvider implements MarketDataProvider {

        private final ProviderProfile profile;
        private final Set<InstrumentHandler> handlers;

        TestProvider(String code, Set<InstrumentHandler> handlers) {
            this.profile = new ProviderProfile(code, "", ProviderDeliveryMode.PULL, ProviderAccessMethod.API_POLL, false, 0);
            this.handlers = handlers;
        }

        @Override
        public ProviderProfile getProfile() {
            return profile;
        }

        @Override
        public Set<InstrumentHandler> getHandlers() {
            return handlers;
        }
    }

    /* Тестовая реализация обработчика. */
    private static class TestHandler implements InstrumentHandler {

        private final String providerCode;
        private final InstrumentType type;

        TestHandler(String providerCode, InstrumentType type) {
            this.providerCode = providerCode;
            this.type = type;
        }

        @Override
        public String providerCode() {
            return providerCode;
        }

        @Override
        public InstrumentType supportedInstrument() {
            return type;
        }

        @Override
        public Flux<QuoteTick> instrumentQuote(Instrument instrument) {
            return Flux.empty();
        }
    }
}

