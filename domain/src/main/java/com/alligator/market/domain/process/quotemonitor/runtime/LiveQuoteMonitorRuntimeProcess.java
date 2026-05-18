package com.alligator.market.domain.process.quotemonitor.runtime;

public interface LiveQuoteMonitorRuntimeProcess {

    boolean start();

    boolean stop();

    LiveQuoteMonitorRuntimeStatus status();

    LiveQuoteMonitorRuntimeSnapshot snapshot();
}
