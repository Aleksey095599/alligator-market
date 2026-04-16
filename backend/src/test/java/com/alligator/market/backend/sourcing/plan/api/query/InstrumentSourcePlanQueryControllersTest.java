package com.alligator.market.backend.sourcing.plan.api.query;

import com.alligator.market.backend.sourcing.config.plan.api.mapper.InstrumentSourcePlanApiMapperWiringConfig;
import com.alligator.market.backend.sourcing.plan.api.query.get.controller.GetInstrumentSourcePlanController;
import com.alligator.market.backend.sourcing.plan.api.query.list.controller.ListInstrumentSourcePlansController;
import com.alligator.market.backend.sourcing.plan.api.query.options.controller.InstrumentSourcePlanOptionsQueryController;
import com.alligator.market.backend.sourcing.plan.application.exception.InstrumentSourcePlanNotFoundException;
import com.alligator.market.backend.sourcing.plan.application.query.get.GetInstrumentSourcePlanService;
import com.alligator.market.backend.sourcing.plan.application.query.list.ListInstrumentSourcePlansService;
import com.alligator.market.backend.sourcing.plan.application.query.options.port.InstrumentOptionsQueryPort;
import com.alligator.market.backend.sourcing.plan.application.query.options.port.ProviderOptionsQueryPort;
import com.alligator.market.domain.instrument.base.vo.InstrumentCode;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.sourcing.plan.InstrumentSourcePlan;
import com.alligator.market.domain.sourcing.source.MarketDataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Интеграционный web-layer тест query-контроллеров плана источников.
 */
@Tag("dev")
@WebMvcTest(controllers = {
        GetInstrumentSourcePlanController.class,
        ListInstrumentSourcePlansController.class,
        InstrumentSourcePlanOptionsQueryController.class
})
@Import(InstrumentSourcePlanApiMapperWiringConfig.class)
class InstrumentSourcePlanQueryControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetInstrumentSourcePlanService getInstrumentSourcePlanService;

    @MockitoBean
    private ListInstrumentSourcePlansService listInstrumentSourcePlansService;

    @MockitoBean
    private InstrumentOptionsQueryPort instrumentOptionsQueryPort;

    @MockitoBean
    private ProviderOptionsQueryPort providerOptionsQueryPort;

    @Test
    @DisplayName("GET /api/v1/instrument-source-plans/{instrumentCode} -> 200 и детальный план")
    void getByInstrumentCodeReturnsPlan() throws Exception {
        InstrumentSourcePlan plan = new InstrumentSourcePlan(
                new InstrumentCode("  eur_usd  "),
                List.of(new MarketDataSource(new ProviderCode("moex"), true, 0))
        );
        given(getInstrumentSourcePlanService.get(new InstrumentCode("EUR_USD"))).willReturn(plan);

        mockMvc.perform(get("/api/v1/instrument-source-plans/EUR_USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instrumentCode").value("EUR_USD"))
                .andExpect(jsonPath("$.sources[0].providerCode").value("MOEX"))
                .andExpect(jsonPath("$.sources[0].active").value(true))
                .andExpect(jsonPath("$.sources[0].priority").value(0));
    }

    @Test
    @DisplayName("GET /api/v1/instrument-source-plans -> 200 и список планов")
    void listReturnsAllPlans() throws Exception {
        InstrumentSourcePlan eurUsdPlan = new InstrumentSourcePlan(
                new InstrumentCode("eur_usd"),
                List.of(new MarketDataSource(new ProviderCode("moex"), true, 0))
        );
        InstrumentSourcePlan gbpUsdPlan = new InstrumentSourcePlan(
                new InstrumentCode("gbp_usd"),
                List.of(new MarketDataSource(new ProviderCode("cme"), false, 1))
        );
        given(listInstrumentSourcePlansService.list()).willReturn(List.of(eurUsdPlan, gbpUsdPlan));

        mockMvc.perform(get("/api/v1/instrument-source-plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plans.length()").value(2))
                .andExpect(jsonPath("$.plans[0].instrumentCode").value("EUR_USD"))
                .andExpect(jsonPath("$.plans[1].instrumentCode").value("GBP_USD"));
    }

    @Test
    @DisplayName("GET /api/v1/instrument-source-plans/options -> 200 и options для dropdown")
    void getOptionsReturnsInstrumentsAndProviders() throws Exception {
        given(instrumentOptionsQueryPort.findAllInstrumentCodes())
                .willReturn(List.of(new InstrumentCode("eur_usd"), new InstrumentCode("xau_usd")));
        given(providerOptionsQueryPort.findAllProviderCodes())
                .willReturn(List.of(new ProviderCode("moex"), new ProviderCode("cme")));

        mockMvc.perform(get("/api/v1/instrument-source-plans/options"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.instruments.length()").value(2))
                .andExpect(jsonPath("$.instruments[0].code").value("EUR_USD"))
                .andExpect(jsonPath("$.providers.length()").value(2))
                .andExpect(jsonPath("$.providers[1].code").value("CME"));
    }

    @Test
    @DisplayName("GET /api/v1/instrument-source-plans/{instrumentCode} -> 404 при отсутствии плана")
    void getByInstrumentCodeReturnsNotFoundWhenPlanMissing() throws Exception {
        given(getInstrumentSourcePlanService.get(new InstrumentCode("USD_JPY")))
                .willThrow(new InstrumentSourcePlanNotFoundException(new InstrumentCode("USD_JPY")));

        mockMvc.perform(get("/api/v1/instrument-source-plans/USD_JPY"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorCode").value("INSTRUMENT_SOURCE_PLAN_NOT_FOUND"));
    }
}
