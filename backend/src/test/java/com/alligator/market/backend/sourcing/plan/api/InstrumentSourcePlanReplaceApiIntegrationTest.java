package com.alligator.market.backend.sourcing.plan.api;

import com.alligator.market.backend.sourcing.plan.api.advice.InstrumentSourcePlanRestExceptionHandler;
import com.alligator.market.backend.sourcing.plan.api.command.create.controller.CreateInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.command.replace.controller.ReplaceInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.query.get.controller.GetInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.application.command.create.CreateInstrumentSourcePlanService;
import com.alligator.market.backend.sourcing.plan.application.port.InstrumentCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.port.ProviderCodeExistencePort;
import com.alligator.market.backend.sourcing.plan.application.command.delete.DeleteInstrumentSourcePlanService;
import com.alligator.market.backend.sourcing.plan.application.command.replace.ReplaceInstrumentSourcePlanService;
import com.alligator.market.backend.sourcing.plan.application.query.get.GetInstrumentSourcePlanService;
import com.alligator.market.backend.sourcing.plan.application.query.list.ListInstrumentSourcePlansService;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.plan.repository.InstrumentSourcePlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционные API-тесты replace-сценария плана источников инструмента.
 */
@WebMvcTest(controllers = {
        CreateInstrumentSourcePlanController.class,
        ReplaceInstrumentSourcePlanController.class,
        GetInstrumentSourcePlanController.class
})
@Import({
        InstrumentSourcePlanRestExceptionHandler.class,
        InstrumentSourcePlanReplaceApiIntegrationTest.TestBeans.class
})
class InstrumentSourcePlanReplaceApiIntegrationTest {

    /* HTTP-клиент тестового слоя MVC. */
    @Autowired
    private MockMvc mockMvc;

    /* Тестовый каталог инструментов. */
    @Autowired
    private InMemoryInstrumentCodeExistencePort instrumentCatalog;

    /* Тестовый каталог провайдеров. */
    @Autowired
    private InMemoryProviderCodeExistencePort providerCatalog;

    /* In-memory репозиторий планов. */
    @Autowired
    private InMemoryInstrumentSourcePlanRepository repository;

    @BeforeEach
    void setUp() {
        repository.clear();
        instrumentCatalog.clear();
        providerCatalog.clear();
    }

    @Test
    void replaceForExistingPlanReturnsNoContentAndGetReturnsNewSources() throws Exception {
        instrumentCatalog.add("EURUSD_TOM");
        providerCatalog.addAll("MOEX", "BLOOMBERG", "REUTERS");

        createInitialPlan();

        mockMvc.perform(put("/api/v1/instrument-source-plans/{instrumentCode}", "EURUSD_TOM")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sources": [
                                    {"providerCode": "REUTERS", "active": true, "priority": 0},
                                    {"providerCode": "MOEX", "active": false, "priority": 1}
                                  ]
                                }
                                """))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/instrument-source-plans/{instrumentCode}", "EURUSD_TOM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instrumentCode").value("EURUSD_TOM"))
                .andExpect(jsonPath("$.sources.length()").value(2))
                .andExpect(jsonPath("$.sources[0].providerCode").value("REUTERS"))
                .andExpect(jsonPath("$.sources[0].priority").value(0))
                .andExpect(jsonPath("$.sources[1].providerCode").value("MOEX"))
                .andExpect(jsonPath("$.sources[1].priority").value(1));
    }

    @Test
    void replaceForMissingPlanReturnsNotFound() throws Exception {
        instrumentCatalog.add("EURUSD_TOM");
        providerCatalog.add("MOEX");

        mockMvc.perform(put("/api/v1/instrument-source-plans/{instrumentCode}", "EURUSD_TOM")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sources": [
                                    {"providerCode": "MOEX", "active": true, "priority": 0}
                                  ]
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("INSTRUMENT_SOURCE_PLAN_NOT_FOUND"));
    }

    @Test
    void replaceWithMissingProviderCodeReturnsBadRequest() throws Exception {
        instrumentCatalog.add("EURUSD_TOM");
        providerCatalog.add("MOEX");
        createInitialPlan();

        mockMvc.perform(put("/api/v1/instrument-source-plans/{instrumentCode}", "EURUSD_TOM")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sources": [
                                    {"providerCode": "MOEX", "active": true, "priority": 0},
                                    {"providerCode": "UNKNOWN_PROVIDER", "active": true, "priority": 1}
                                  ]
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("PROVIDER_CODES_NOT_FOUND"));
    }

    @Test
    void replaceRemovesOldSourcesInsteadOfAppending() throws Exception {
        instrumentCatalog.add("EURUSD_TOM");
        providerCatalog.addAll("MOEX", "BLOOMBERG", "REUTERS");
        createInitialPlan();

        mockMvc.perform(put("/api/v1/instrument-source-plans/{instrumentCode}", "EURUSD_TOM")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sources": [
                                    {"providerCode": "REUTERS", "active": true, "priority": 0}
                                  ]
                                }
                                """))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/instrument-source-plans/{instrumentCode}", "EURUSD_TOM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sources.length()").value(1))
                .andExpect(jsonPath("$.sources[0].providerCode").value("REUTERS"));
    }

    /* Подготовка исходного плана для replace-сценариев. */
    private void createInitialPlan() throws Exception {
        mockMvc.perform(post("/api/v1/instrument-source-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "instrumentCode": "EURUSD_TOM",
                                  "sources": [
                                    {"providerCode": "MOEX", "active": true, "priority": 0},
                                    {"providerCode": "BLOOMBERG", "active": true, "priority": 1}
                                  ]
                                }
                                """))
                .andExpect(status().isCreated());
    }

    /**
     * Тестовая wiring-конфигурация для интеграции API + application слоя без внешней БД.
     */
    @TestConfiguration
    static class TestBeans {

        @Bean
        InMemoryInstrumentSourcePlanRepository instrumentSourcePlanRepository() {
            return new InMemoryInstrumentSourcePlanRepository();
        }

        @Bean
        InstrumentCodeExistencePort instrumentCodeExistencePort() {
            return new InMemoryInstrumentCodeExistencePort();
        }

        @Bean
        ProviderCodeExistencePort providerCodeExistencePort() {
            return new InMemoryProviderCodeExistencePort();
        }

        @Bean
        CreateInstrumentSourcePlanService createInstrumentSourcePlanService(
                InstrumentSourcePlanRepository repository,
                InstrumentCodeExistencePort instrumentCodeExistencePort,
                ProviderCodeExistencePort providerCodeExistencePort
        ) {
            return new CreateInstrumentSourcePlanService(repository, instrumentCodeExistencePort, providerCodeExistencePort);
        }

        @Bean
        ReplaceInstrumentSourcePlanService replaceInstrumentSourcePlanService(
                InstrumentSourcePlanRepository repository,
                InstrumentCodeExistencePort instrumentCodeExistencePort,
                ProviderCodeExistencePort providerCodeExistencePort
        ) {
            return new ReplaceInstrumentSourcePlanService(repository, instrumentCodeExistencePort, providerCodeExistencePort);
        }

        @Bean
        GetInstrumentSourcePlanService getInstrumentSourcePlanService(InstrumentSourcePlanRepository repository) {
            return new GetInstrumentSourcePlanService(repository);
        }

        @Bean
        ListInstrumentSourcePlansService listInstrumentSourcePlansService(InstrumentSourcePlanRepository repository) {
            return new ListInstrumentSourcePlansService(repository);
        }

        @Bean
        DeleteInstrumentSourcePlanService deleteInstrumentSourcePlanService(InstrumentSourcePlanRepository repository) {
            return new DeleteInstrumentSourcePlanService(repository);
        }
    }

    /**
     * In-memory реализация репозитория планов для API-тестов.
     */
    static final class InMemoryInstrumentSourcePlanRepository implements InstrumentSourcePlanRepository {

        private final Map<InstrumentCode, InstrumentSourcePlan> plans = new HashMap<>();

        @Override
        public Optional<InstrumentSourcePlan> findByInstrumentCode(InstrumentCode instrumentCode) {
            return Optional.ofNullable(plans.get(instrumentCode));
        }

        @Override
        public List<InstrumentSourcePlan> findAll() {
            return List.copyOf(plans.values());
        }

        @Override
        public boolean createIfAbsent(InstrumentSourcePlan plan) {
            if (plans.containsKey(plan.instrumentCode())) {
                return false;
            }
            plans.put(plan.instrumentCode(), plan);
            return true;
        }

        @Override
        public void replace(InstrumentSourcePlan plan) {
            plans.put(plan.instrumentCode(), plan);
        }

        @Override
        public boolean deleteByInstrumentCode(InstrumentCode instrumentCode) {
            return plans.remove(instrumentCode) != null;
        }

        void clear() {
            plans.clear();
        }
    }

    /**
     * In-memory каталог существующих инструментов.
     */
    static final class InMemoryInstrumentCodeExistencePort implements InstrumentCodeExistencePort {

        private final Set<InstrumentCode> existingCodes = new HashSet<>();

        @Override
        public boolean existsByCode(InstrumentCode instrumentCode) {
            return existingCodes.contains(instrumentCode);
        }

        void add(String code) {
            existingCodes.add(new InstrumentCode(code));
        }

        void clear() {
            existingCodes.clear();
        }
    }

    /**
     * In-memory каталог существующих провайдеров.
     */
    static final class InMemoryProviderCodeExistencePort implements ProviderCodeExistencePort {

        private final Set<ProviderCode> existingCodes = new HashSet<>();

        @Override
        public boolean existsByCode(ProviderCode providerCode) {
            return existingCodes.contains(providerCode);
        }

        void add(String code) {
            existingCodes.add(new ProviderCode(code));
        }

        void addAll(String... codes) {
            for (String code : codes) {
                add(code);
            }
        }

        void clear() {
            existingCodes.clear();
        }
    }
}
