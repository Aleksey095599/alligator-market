package com.alligator.market.backend.provider.profile.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.provider.profile.dto.ProviderProfileDto;
import com.alligator.market.backend.provider.profile.service.ProviderProfileService;
import com.alligator.market.domain.provider.profile.ProviderProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер для чтения профилей провайдеров.
 */
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderProfileController {

    private final ProviderProfileService service;

    /** Вернуть все активные профили провайдеров. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProviderProfileDto>>> getAll() {
        List<ProviderProfileDto> list = service.findAllActive().keySet().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }

    /* Утилита преобразует доменную модель в DTO. */
    private ProviderProfileDto toDto(ProviderProfile profile) {
        return new ProviderProfileDto(
                profile.providerCode(),
                profile.displayName(),
                profile.instrumentTypes(),
                profile.deliveryMode(),
                profile.accessMethod(),
                profile.supportsBulkSubscription(),
                profile.minPollPeriodMs()
        );
    }
}
