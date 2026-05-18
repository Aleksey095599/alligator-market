package com.alligator.market.backend.process.quotemonitor.runtime;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class ScheduledExecutorLiveQuoteMonitorProcessScheduler
        implements LiveQuoteMonitorProcessScheduler, AutoCloseable {
    private final ScheduledExecutorService executor;

    public ScheduledExecutorLiveQuoteMonitorProcessScheduler() {
        this.executor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "live-quote-monitor-runtime");
            thread.setDaemon(true);
            return thread;
        });
    }

    @Override
    public LiveQuoteMonitorScheduledTask scheduleAtFixedRate(Duration interval, Runnable task) {
        Objects.requireNonNull(interval, "interval must not be null");
        Objects.requireNonNull(task, "task must not be null");

        long intervalMillis = interval.toMillis();
        if (intervalMillis <= 0) {
            throw new IllegalArgumentException("interval must be positive");
        }

        ScheduledFuture<?> future = executor.scheduleAtFixedRate(
                task,
                0,
                intervalMillis,
                TimeUnit.MILLISECONDS
        );

        return () -> future.cancel(false);
    }

    @Override
    public void close() {
        executor.shutdownNow();
    }
}
