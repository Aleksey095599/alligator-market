package com.alligator.market.backend.instrument.asset.forex.fxspot.application.command;

import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.create.CreateFxSpotCommand;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.create.CreateFxSpotService;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.delete.DeleteFxSpotService;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.update.UpdateFxSpotCommand;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.command.update.UpdateFxSpotService;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpot;
import com.alligator.market.domain.instrument.asset.forex.fxspot.FxSpotTenor;
import com.alligator.market.domain.instrument.asset.forex.fxspot.repository.FxSpotRepository;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.Currency;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.repository.CurrencyRepository;
import com.alligator.market.domain.instrument.asset.forex.reference.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.registry.sync.RuntimeInstrumentRegistryUpdater;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FxSpotRuntimeInstrumentRegistrySynchronizationTest {

    @Test
    void createSynchronizesRuntimeInstrumentRegistryAfterStoredInstrumentChanges() {
        FakeFxSpotRepository fxSpotRepository = new FakeFxSpotRepository();
        FakeRuntimeInstrumentRegistryUpdater runtimeRegistryUpdater =
                new FakeRuntimeInstrumentRegistryUpdater();
        CreateFxSpotService service = new CreateFxSpotService(
                fxSpotRepository,
                new FakeCurrencyRepository(List.of(cny(), rub())),
                runtimeRegistryUpdater
        );

        FxSpot created = service.create(new CreateFxSpotCommand(
                CurrencyCode.of("CNY"),
                CurrencyCode.of("RUB"),
                FxSpotTenor.TOM,
                4
        ));

        assertEquals(1, runtimeRegistryUpdater.updateCount());
        assertEquals(Optional.of(created), fxSpotRepository.findByCode(created.instrumentCode()));
    }

    @Test
    void updateSynchronizesRuntimeInstrumentRegistryAfterInstrumentChanges() {
        FxSpot current = fxSpot(4);
        FakeFxSpotRepository fxSpotRepository = new FakeFxSpotRepository(List.of(current));
        FakeRuntimeInstrumentRegistryUpdater runtimeRegistryUpdater =
                new FakeRuntimeInstrumentRegistryUpdater();
        UpdateFxSpotService service = new UpdateFxSpotService(
                fxSpotRepository,
                runtimeRegistryUpdater
        );

        service.update(new UpdateFxSpotCommand(current.instrumentCode(), 5));

        assertEquals(1, runtimeRegistryUpdater.updateCount());
        assertEquals(
                5,
                fxSpotRepository.findByCode(current.instrumentCode())
                        .orElseThrow()
                        .defaultQuoteFractionDigits()
        );
    }

    @Test
    void updateSynchronizesRuntimeInstrumentRegistryWhenNoInstrumentChangesDetected() {
        FxSpot current = fxSpot(4);
        FakeFxSpotRepository fxSpotRepository = new FakeFxSpotRepository(List.of(current));
        FakeRuntimeInstrumentRegistryUpdater runtimeRegistryUpdater =
                new FakeRuntimeInstrumentRegistryUpdater();
        UpdateFxSpotService service = new UpdateFxSpotService(
                fxSpotRepository,
                runtimeRegistryUpdater
        );

        service.update(new UpdateFxSpotCommand(current.instrumentCode(), 4));

        assertEquals(1, runtimeRegistryUpdater.updateCount());
        assertEquals(Optional.of(current), fxSpotRepository.findByCode(current.instrumentCode()));
    }

    @Test
    void deleteSynchronizesRuntimeInstrumentRegistryAfterStoredInstrumentChanges() {
        FxSpot current = fxSpot(4);
        FakeFxSpotRepository fxSpotRepository = new FakeFxSpotRepository(List.of(current));
        FakeRuntimeInstrumentRegistryUpdater runtimeRegistryUpdater =
                new FakeRuntimeInstrumentRegistryUpdater();
        DeleteFxSpotService service = new DeleteFxSpotService(
                fxSpotRepository,
                instrumentCode -> false,
                runtimeRegistryUpdater
        );

        service.delete(current.instrumentCode());

        assertEquals(1, runtimeRegistryUpdater.updateCount());
        assertTrue(fxSpotRepository.findAll().isEmpty());
    }

    private static FxSpot fxSpot(int defaultQuoteFractionDigits) {
        return new FxSpot(cny(), rub(), FxSpotTenor.TOM, defaultQuoteFractionDigits);
    }

    private static Currency cny() {
        return new Currency(CurrencyCode.of("CNY"), "Chinese Yuan", "China", 2);
    }

    private static Currency rub() {
        return new Currency(CurrencyCode.of("RUB"), "Russian Ruble", "Russia", 2);
    }

    private static final class FakeRuntimeInstrumentRegistryUpdater
            implements RuntimeInstrumentRegistryUpdater {
        private int updateCount;

        @Override
        public void updateRuntimeRegistry() {
            updateCount++;
        }

        private int updateCount() {
            return updateCount;
        }
    }

    private static final class FakeCurrencyRepository implements CurrencyRepository {
        private final Map<CurrencyCode, Currency> currencies = new LinkedHashMap<>();

        private FakeCurrencyRepository(List<Currency> currencies) {
            Objects.requireNonNull(currencies, "currencies must not be null")
                    .forEach(currency -> this.currencies.put(currency.code(), currency));
        }

        @Override
        public Currency create(Currency currency) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Currency update(Currency currency) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteByCode(CurrencyCode code) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<Currency> findByCode(CurrencyCode code) {
            return Optional.ofNullable(currencies.get(code));
        }

        @Override
        public List<Currency> findAll() {
            return List.copyOf(currencies.values());
        }
    }

    private static final class FakeFxSpotRepository implements FxSpotRepository {
        private final Map<InstrumentCode, FxSpot> instruments = new LinkedHashMap<>();

        private FakeFxSpotRepository() {
        }

        private FakeFxSpotRepository(List<FxSpot> instruments) {
            Objects.requireNonNull(instruments, "instruments must not be null")
                    .forEach(instrument -> this.instruments.put(
                            instrument.instrumentCode(),
                            instrument
                    ));
        }

        @Override
        public FxSpot create(FxSpot fxSpot) {
            instruments.put(fxSpot.instrumentCode(), fxSpot);
            return fxSpot;
        }

        @Override
        public FxSpot update(FxSpot fxSpot) {
            instruments.put(fxSpot.instrumentCode(), fxSpot);
            return fxSpot;
        }

        @Override
        public void deleteByCode(InstrumentCode instrumentCode) {
            instruments.remove(instrumentCode);
        }

        @Override
        public Optional<FxSpot> findByCode(InstrumentCode instrumentCode) {
            return Optional.ofNullable(instruments.get(instrumentCode));
        }

        @Override
        public List<FxSpot> findAll() {
            return List.copyOf(instruments.values());
        }
    }
}
