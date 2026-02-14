package com.alligator.market.backend.config.time;

import com.alligator.market.backend.time.AppTimeProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Конфигурация бизнес-времени приложения: бизнес-зона и единый источник "now".
 *
 * <p>Назначение: дать коду приложения единый, тестируемый источник текущего времени и явно заданную
 * бизнес-временную зону для календарной логики.</p>
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AppTimeProperties.class)
public class AppTimeConfig {

    public static final String BEAN_APP_BUSINESS_ZONE_ID = "appBusinessZoneId";
    public static final String BEAN_APP_CLOCK = "appClock";

    /**
     * Бизнес-временная зона приложения (IANA ZoneId, например: "UTC").
     *
     * <p>Примечание: Данные считываются из настроек приложения с помощью {@link AppTimeProperties}.</p>
     */
    @Bean(BEAN_APP_BUSINESS_ZONE_ID)
    public ZoneId appBusinessZoneId(AppTimeProperties props) {
        return ZoneId.of(props.businessZoneId());
    }

    /**
     * Часы приложения — использовать во всём коде вместо {@code Instant.now()} / {@code ZonedDateTime.now()}.
     *
     * <p>Важно: {@link Clock} нужен не для хранения времени (в БД всё равно {@code Instant}),
     * а для единообразного получения "now" и корректной календарной логики в бизнес-зоне.</p>
     */
    @Bean(BEAN_APP_CLOCK)
    public Clock appClock(@Qualifier(BEAN_APP_BUSINESS_ZONE_ID) ZoneId appBusinessZoneId) {
        return Clock.system(appBusinessZoneId);
    }
}
