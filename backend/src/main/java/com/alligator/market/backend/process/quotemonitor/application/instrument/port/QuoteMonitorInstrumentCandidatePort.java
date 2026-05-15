package com.alligator.market.backend.process.quotemonitor.application.instrument.port;

import com.alligator.market.domain.instrument.vo.InstrumentCode;

import java.util.List;

public interface QuoteMonitorInstrumentCandidatePort {
    List<InstrumentCode> findCandidateInstrumentCodes();

    List<InstrumentCode> findMissingCandidateInstrumentCodes(List<InstrumentCode> instrumentCodes);
}
