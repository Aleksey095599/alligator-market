package com.alligator.market.backend.process.quotemonitor.config.instrument;

import com.alligator.market.backend.process.quotemonitor.application.instrument.QuoteMonitorInstrumentSelectionService;
import com.alligator.market.backend.process.quotemonitor.application.instrument.port.QuoteMonitorInstrumentCandidatePort;
import com.alligator.market.backend.process.quotemonitor.config.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryUpdaterWiringConfig;
import com.alligator.market.backend.process.quotemonitor.config.runtime.QuoteMonitorRuntimeProcessWiringConfig;
import com.alligator.market.backend.process.quotemonitor.persistence.jooq.instrument.JooqQuoteMonitorInstrumentCandidateAdapter;
import com.alligator.market.backend.process.quotemonitor.persistence.jooq.instrument.JooqQuoteMonitorInstrumentSelectionRepository;
import com.alligator.market.backend.sourceplan.config.plan.application.query.common.SourcePlanQueryPortWiringConfig;
import com.alligator.market.backend.sourceplan.plan.application.query.common.port.SourcePlanQueryPort;
import com.alligator.market.domain.process.quotemonitor.instrument.repository.QuoteMonitorInstrumentSelectionRepository;
import com.alligator.market.domain.process.quotemonitor.instrument.registry.sync.RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater;
import com.alligator.market.domain.process.quotemonitor.runtime.QuoteMonitorRuntimeProcess;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@Import({
        RuntimeQuoteMonitorInstrumentSelectionRegistryUpdaterWiringConfig.class,
        QuoteMonitorRuntimeProcessWiringConfig.class,
        SourcePlanQueryPortWiringConfig.class
})
public class QuoteMonitorInstrumentSelectionWiringConfig {
    public static final String BEAN_QUOTE_MONITOR_INSTRUMENT_SELECTION_REPOSITORY =
            "quoteMonitorInstrumentSelectionRepository";
    public static final String BEAN_QUOTE_MONITOR_INSTRUMENT_CANDIDATE_PORT =
            "quoteMonitorInstrumentCandidatePort";
    public static final String BEAN_QUOTE_MONITOR_INSTRUMENT_SELECTION_SERVICE =
            "quoteMonitorInstrumentSelectionService";

    @Bean(BEAN_QUOTE_MONITOR_INSTRUMENT_SELECTION_REPOSITORY)
    public QuoteMonitorInstrumentSelectionRepository quoteMonitorInstrumentSelectionRepository(DSLContext dsl) {
        return new JooqQuoteMonitorInstrumentSelectionRepository(dsl);
    }

    @Bean(BEAN_QUOTE_MONITOR_INSTRUMENT_CANDIDATE_PORT)
    public QuoteMonitorInstrumentCandidatePort quoteMonitorInstrumentCandidatePort(DSLContext dsl) {
        return new JooqQuoteMonitorInstrumentCandidateAdapter(dsl);
    }

    @Bean(BEAN_QUOTE_MONITOR_INSTRUMENT_SELECTION_SERVICE)
    public QuoteMonitorInstrumentSelectionService quoteMonitorInstrumentSelectionService(
            @Qualifier(BEAN_QUOTE_MONITOR_INSTRUMENT_SELECTION_REPOSITORY)
            QuoteMonitorInstrumentSelectionRepository repository,
            @Qualifier(BEAN_QUOTE_MONITOR_INSTRUMENT_CANDIDATE_PORT)
            QuoteMonitorInstrumentCandidatePort candidatePort,
            @Qualifier(SourcePlanQueryPortWiringConfig.BEAN_SOURCE_PLAN_QUERY_PORT)
            SourcePlanQueryPort sourcePlanQueryPort,
            @Qualifier(
                    RuntimeQuoteMonitorInstrumentSelectionRegistryUpdaterWiringConfig
                            .BEAN_RUNTIME_QUOTE_MONITOR_INSTRUMENT_SELECTION_REGISTRY_UPDATER
            )
            RuntimeQuoteMonitorInstrumentSelectionRegistryUpdater runtimeRegistryUpdater,
            @Qualifier(QuoteMonitorRuntimeProcessWiringConfig.BEAN_QUOTE_MONITOR_RUNTIME_PROCESS)
            QuoteMonitorRuntimeProcess runtimeProcess
    ) {
        return new QuoteMonitorInstrumentSelectionService(
                repository,
                candidatePort,
                sourcePlanQueryPort,
                runtimeRegistryUpdater,
                runtimeProcess
        );
    }
}
