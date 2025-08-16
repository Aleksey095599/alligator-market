package com.alligator.market.backend.provider.profile.catalog.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.provider.profile.catalog.web.dto.ProviderProfileDto;
import com.alligator.market.backend.provider.profile.catalog.web.dto.ProviderProfileStatusDto;
import com.alligator.market.backend.provider.profile.catalog.service.ProviderProfileService;
import com.alligator.market.domain.provider.profile.model.ProviderProfile;
import com.alligator.market.domain.provider.model.ProviderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер профилей провайдеров рыночных данных (далее - профили).
 */
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderProfileController {

    private final ProviderProfileService service;

    //==============================================
    //                  Операции
    //==============================================

    /** Вернуть все активные профили. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProviderProfileDto>>> getAll() {
        List<ProviderProfileDto> list = service.findAllActive().values().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }

    /** Вернуть все профили со статусами. */
    @GetMapping("/audit")
    public ResponseEntity<ApiResponse<List<ProviderProfileStatusDto>>> getAllWithStatus() {
        List<ProviderProfileStatusDto> list = service.findAllWithStatus().entrySet().stream()
                .map(e -> toStatusDto(e.getKey(), e.getValue()))
                .toList();
        return ResponseEntityFactory.ok(list);
    }

    //==============================================
    //                   Утилиты
    //==============================================

    /** Утилита преобразует доменную модель в DTO. */
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

    /** Утилита преобразует доменную модель и статус в DTO. */
    private ProviderProfileStatusDto toStatusDto(ProviderProfile profile, ProviderStatus status) {
        return new ProviderProfileStatusDto(
                profile.providerCode(),
                profile.displayName(),
                profile.instrumentTypes(),
                profile.deliveryMode(),
                profile.accessMethod(),
                profile.supportsBulkSubscription(),
                profile.minPollPeriodMs(),
                status
        );
    }
}
