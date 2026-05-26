package com.alligator.market.domain.process.quotemonitor.runtime;

public interface QuoteMonitorRuntimeProcess {

    boolean start();

    boolean stop();

    QuoteMonitorRuntimeStatus status();

    QuoteMonitorRuntimeSnapshot snapshot();
}
