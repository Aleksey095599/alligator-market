package com.alligator.market.backend.config.time;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

/**
 * Поставщик текущего времени в UTC.
 */
@Component
public class TimeZoneDateTimeProvider implements DateTimeProvider {

    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(Instant.now());
    }
}
