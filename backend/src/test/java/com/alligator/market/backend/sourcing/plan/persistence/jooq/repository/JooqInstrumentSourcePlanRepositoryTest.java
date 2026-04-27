package com.alligator.market.backend.sourcing.plan.persistence.jooq.repository;

import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentCodeNotFoundException;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.provider.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Тесты jOOQ-адаптера репозитория плана источников.
 */
@Tag("dev")
class JooqInstrumentSourcePlanRepositoryTest {

    @Test
    void shouldMapInstrumentFkViolationToInstrumentCodeNotFoundException() {
        /* Мокаем DSLContext как persistence-границу. */
        DSLContext dsl = mock(DSLContext.class);

        /* FK violation с именем целевого constraint в cause-цепочке. */
        DataIntegrityViolationException violation = new DataIntegrityViolationException(
                "insert failed",
                new IllegalStateException("violates constraint fk_instr_source_plan_instrument")
        );

        given(dsl.transactionResult(any())).willThrow(violation);

        JooqInstrumentSourcePlanRepository repository = new JooqInstrumentSourcePlanRepository(dsl);
        InstrumentSourcePlan plan = sourcePlan("EUR_USD");

        assertThrows(InstrumentCodeNotFoundException.class, () -> repository.createIfAbsent(plan));
    }

    @Test
    void shouldRethrowUnknownDataIntegrityViolation() {
        /* Мокаем DSLContext как persistence-границу. */
        DSLContext dsl = mock(DSLContext.class);

        /* Нарушение другого ограничения не должно маппиться в application-исключение. */
        DataIntegrityViolationException violation = new DataIntegrityViolationException(
                "insert failed",
                new IllegalStateException("violates constraint uq_market_data_source_instr_provider")
        );

        given(dsl.transactionResult(any())).willThrow(violation);

        JooqInstrumentSourcePlanRepository repository = new JooqInstrumentSourcePlanRepository(dsl);
        InstrumentSourcePlan plan = sourcePlan("EUR_USD");

        DataIntegrityViolationException actual =
                assertThrows(DataIntegrityViolationException.class, () -> repository.createIfAbsent(plan));

        assertSame(violation, actual);
    }

    @Test
    void shouldReturnFalseWhenPlanAlreadyExists() {
        /* Проверяем сохранение контракта createIfAbsent при отсутствии вставки. */
        DSLContext dsl = mock(DSLContext.class);
        given(dsl.transactionResult(any())).willReturn(false);

        JooqInstrumentSourcePlanRepository repository = new JooqInstrumentSourcePlanRepository(dsl);

        boolean created = repository.createIfAbsent(sourcePlan("EUR_USD"));

        assertFalse(created);
    }

    /* Создает валидный aggregate для тестовых сценариев. */
    private InstrumentSourcePlan sourcePlan(String instrumentCode) {
        return new InstrumentSourcePlan(
                new InstrumentCode(instrumentCode),
                List.of(new MarketDataSource(new ProviderCode("MOEX"), true, 0))
        );
    }
}
