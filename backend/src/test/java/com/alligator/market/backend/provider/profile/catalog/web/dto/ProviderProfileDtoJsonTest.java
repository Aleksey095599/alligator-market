package com.alligator.market.backend.provider.profile.catalog.web.dto;

import com.alligator.market.domain.instrument.model.InstrumentType;
import com.alligator.market.domain.provider.profile.context.ProviderProfileStatus;
import com.alligator.market.domain.provider.profile.model.ProviderAccessMethod;
import com.alligator.market.domain.provider.profile.model.ProviderDeliveryMode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тест JSON сериализации и десериализации DTO профиля провайдера.
 */
class ProviderProfileDtoJsonTest {

    private final ObjectMapper mapper = new ObjectMapper();

    /** Проверяем профиль без статуса. */
    @Test
    void shouldSerializeAndDeserializeProfile() throws Exception {
        ProviderProfileDto dto = new ProviderProfileDto(
                "code",
                "name",
                Set.of(InstrumentType.FOREX),
                ProviderDeliveryMode.PUSH,
                ProviderAccessMethod.API_POLL,
                true,
                42
        );
        String json = mapper.writeValueAsString(dto);
        assertTrue(json.contains("\"deliveryMode\""));
        assertTrue(json.contains("\"accessMethod\""));
        assertTrue(json.contains("\"bulkSubscription\""));
        assertTrue(json.contains("\"minPollMs\""));

        ProviderProfileDto restored = mapper.readValue(json, ProviderProfileDto.class);
        assertEquals(dto, restored);
    }

    /** Проверяем профиль со статусом. */
    @Test
    void shouldSerializeAndDeserializeStatusProfile() throws Exception {
        ProviderProfileStatusDto dto = new ProviderProfileStatusDto(
                ProviderProfileStatus.ACTIVE,
                "code",
                "name",
                Set.of(InstrumentType.FOREX),
                ProviderDeliveryMode.PUSH,
                ProviderAccessMethod.API_POLL,
                true,
                42
        );
        String json = mapper.writeValueAsString(dto);
        assertTrue(json.contains("\"status\""));
        assertTrue(json.contains("\"deliveryMode\""));
        assertTrue(json.contains("\"accessMethod\""));
        assertTrue(json.contains("\"bulkSubscription\""));
        assertTrue(json.contains("\"minPollMs\""));

        ProviderProfileStatusDto restored = mapper.readValue(json, ProviderProfileStatusDto.class);
        assertEquals(dto, restored);
    }
}
