package com.alligator.market.backend.process.quotemonitor.application.instrument.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;

public interface QuoteMonitorInstrumentPort {
    List<InstrumentCode> findAvailableInstrumentCodes();

    List<InstrumentCode> findSelectedInstrumentCodes();

    List<InstrumentCode> findInstrumentCodesWithoutSourcePlan(List<InstrumentCode> instrumentCodes);

    void replaceSelectedInstrumentCodes(List<InstrumentCode> instrumentCodes);
}
