package com.alligator.market.backend.infra.time;

import com.alligator.market.backend.infra.time.prop.AppTimeProperties;
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
     * Часы приложения:
     */
    @Bean(BEAN_APP_CLOCK)
    public Clock appClock(@Qualifier(BEAN_APP_BUSINESS_ZONE_ID) ZoneId appBusinessZoneId) {
        return Clock.system(appBusinessZoneId);
    }
}
