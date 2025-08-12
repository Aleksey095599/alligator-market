package com.alligator.market.backend.config.time;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;

/**
 * Поставщик текущего времени с учётом настроенной временной зоны.
 */
@Component
public class TimeZoneDateTimeProvider implements DateTimeProvider {

    private final AppTimeProps props;

    // Конструктор
    public TimeZoneDateTimeProvider(AppTimeProps props) {
        this.props = props;
    }

    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(OffsetDateTime.now(props.timeZone()));
    }
}
