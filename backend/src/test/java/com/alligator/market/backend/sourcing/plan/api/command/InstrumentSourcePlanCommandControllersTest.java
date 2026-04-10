package com.alligator.market.backend.sourcing.plan.api.command;

import com.alligator.market.backend.sourcing.plan.api.command.create.controller.CreateInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.command.delete.controller.DeleteInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.command.replace.controller.ReplaceInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.application.command.create.CreateInstrumentSourcePlanService;
import com.alligator.market.backend.sourcing.plan.application.command.delete.DeleteInstrumentSourcePlanService;
import com.alligator.market.backend.sourcing.plan.application.command.replace.ReplaceInstrumentSourcePlanService;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanAlreadyExistsException;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.exception.ProviderCodesNotFoundException;
import com.alligator.market.domain.instrument.base.model.vo.InstrumentCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Интеграционный web-layer тест command-контроллеров плана источников.
 */
@Tag("manual")
@WebMvcTest(controllers = {
        CreateInstrumentSourcePlanController.class,
        ReplaceInstrumentSourcePlanController.class,
        DeleteInstrumentSourcePlanController.class
})
class InstrumentSourcePlanCommandControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateInstrumentSourcePlanService createInstrumentSourcePlanService;

    @MockitoBean
    private ReplaceInstrumentSourcePlanService replaceInstrumentSourcePlanService;

    @MockitoBean
    private DeleteInstrumentSourcePlanService deleteInstrumentSourcePlanService;

    @Test
    @DisplayName("POST /api/v1/instrument-source-plans -> 201 и корректный маппинг запроса в домен")
    void createReturnsCreatedAndMapsRequestToDomainPlan() throws Exception {
        String request = """
                {
                  "instrumentCode": " eur_usd ",
                  "sources": [
                    {"providerCode": "moex", "active": true, "priority": 0},
                    {"providerCode": "cme", "active": false, "priority": 1}
                  ]
                }
                """;

        mockMvc.perform(post("/api/v1/instrument-source-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated());

        ArgumentCaptor<InstrumentSourcePlan> planCaptor = ArgumentCaptor.forClass(InstrumentSourcePlan.class);
        then(createInstrumentSourcePlanService).should().create(planCaptor.capture());
        InstrumentSourcePlan actualPlan = planCaptor.getValue();

        assertThat(actualPlan.instrumentCode().value()).isEqualTo("EUR_USD");
        assertThat(actualPlan.sources()).hasSize(2);
        assertThat(actualPlan.sources().get(0).providerCode().value()).isEqualTo("MOEX");
        assertThat(actualPlan.sources().get(0).priority()).isEqualTo(0);
        assertThat(actualPlan.sources().get(1).providerCode().value()).isEqualTo("CME");
    }

    @Test
    @DisplayName("POST /api/v1/instrument-source-plans -> 422 при невалидном теле")
    void createReturnsValidationErrorWhenBodyInvalid() throws Exception {
        String invalidRequest = """
                {
                  "instrumentCode": " ",
                  "sources": []
                }
                """;

        mockMvc.perform(post("/api/v1/instrument-source-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
    }

    @Test
    @DisplayName("POST /api/v1/instrument-source-plans -> 409 если план уже существует")
    void createReturnsConflictWhenPlanAlreadyExists() throws Exception {
        doThrow(new InstrumentSourcePlanAlreadyExistsException(new InstrumentCode("EUR_USD")))
                .when(createInstrumentSourcePlanService)
                .create(any(InstrumentSourcePlan.class));

        String request = """
                {
                  "instrumentCode": "EUR_USD",
                  "sources": [
                    {"providerCode": "MOEX", "active": true, "priority": 0}
                  ]
                }
                """;

        mockMvc.perform(post("/api/v1/instrument-source-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("INSTRUMENT_SOURCE_PLAN_ALREADY_EXISTS"));
    }

    @Test
    @DisplayName("PUT /api/v1/instrument-source-plans/{instrumentCode} -> 204")
    void replaceReturnsNoContent() throws Exception {
        String request = """
                {
                  "sources": [
                    {"providerCode": "cme", "active": true, "priority": 0}
                  ]
                }
                """;

        mockMvc.perform(put("/api/v1/instrument-source-plans/EUR_USD")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNoContent());

        ArgumentCaptor<InstrumentSourcePlan> planCaptor = ArgumentCaptor.forClass(InstrumentSourcePlan.class);
        then(replaceInstrumentSourcePlanService).should().replace(planCaptor.capture());
        assertThat(planCaptor.getValue().instrumentCode().value()).isEqualTo("EUR_USD");
    }

    @Test
    @DisplayName("PUT /api/v1/instrument-source-plans/{instrumentCode} -> 400 для отсутствующего provider")
    void replaceReturnsBadRequestForUnknownProvider() throws Exception {
        doThrow(new ProviderCodesNotFoundException(List.of("UNKNOWN_PROVIDER")))
                .when(replaceInstrumentSourcePlanService)
                .replace(any(InstrumentSourcePlan.class));

        String request = """
                {
                  "sources": [
                    {"providerCode": "unknown_provider", "active": true, "priority": 0}
                  ]
                }
                """;

        mockMvc.perform(put("/api/v1/instrument-source-plans/EUR_USD")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("PROVIDER_CODES_NOT_FOUND"));
    }

    @Test
    @DisplayName("DELETE /api/v1/instrument-source-plans/{instrumentCode} -> 204")
    void deleteReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/instrument-source-plans/EUR_USD"))
                .andExpect(status().isNoContent());

        then(deleteInstrumentSourcePlanService).should().delete(new InstrumentCode("EUR_USD"));
    }

    @Test
    @DisplayName("DELETE /api/v1/instrument-source-plans/{instrumentCode} -> 404 при отсутствии плана")
    void deleteReturnsNotFoundWhenPlanMissing() throws Exception {
        doThrow(new InstrumentSourcePlanNotFoundException(new InstrumentCode("EUR_USD")))
                .when(deleteInstrumentSourcePlanService)
                .delete(new InstrumentCode("EUR_USD"));

        mockMvc.perform(delete("/api/v1/instrument-source-plans/EUR_USD"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("INSTRUMENT_SOURCE_PLAN_NOT_FOUND"));
    }
}
