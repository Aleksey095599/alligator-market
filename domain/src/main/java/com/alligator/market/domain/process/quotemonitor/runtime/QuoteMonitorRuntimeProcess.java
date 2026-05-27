package com.alligator.market.domain.process.quotemonitor.runtime;

import com.alligator.market.domain.process.quotemonitor.runtime.state.QuoteMonitorRuntimeState;
import com.alligator.market.domain.process.quotemonitor.runtime.state.QuoteMonitorRuntimeStatus;

public interface QuoteMonitorRuntimeProcess {

    boolean start();

    boolean stop();

    QuoteMonitorRuntimeStatus status();

    QuoteMonitorRuntimeState state();
}
