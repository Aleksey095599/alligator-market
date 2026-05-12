package com.alligator.market.backend.source.config.passport.persistence.projection.port.adapter;

import com.alligator.market.backend.source.passport.application.projection.port.SourcePassportProjectionWritePort;
import com.alligator.market.backend.source.passport.persistence.projection.port.adapter.JooqSourcePassportProjectionWritePortAdapter;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class SourcePassportProjectionWritePortWiringConfig {
    public static final String BEAN_SOURCE_PASSPORT_PROJECTION_WRITE_PORT =
            "sourcePassportProjectionWritePort";

    @Bean(BEAN_SOURCE_PASSPORT_PROJECTION_WRITE_PORT)
    public SourcePassportProjectionWritePort sourcePassportProjectionWritePort(DSLContext dsl) {
        return new JooqSourcePassportProjectionWritePortAdapter(dsl);
    }
}
