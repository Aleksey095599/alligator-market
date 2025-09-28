package com.alligator.market.backend.provider.catalog.settings.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.provider.catalog.settings.service.ProviderSettingsUseCase;
import com.alligator.market.backend.provider.catalog.settings.web.dto.ProviderSettingsDto;
import com.alligator.market.backend.provider.catalog.settings.web.dto.ProviderSettingsDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер настроек провайдеров.
 */
@RestController
@RequestMapping("/api/v1/provider-settings")
@RequiredArgsConstructor
public class ProviderSettingsController {

    private final ProviderSettingsUseCase service;
    private final ProviderSettingsDtoMapper mapper;

    /** Вернуть все настройки. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProviderSettingsDto>>> getAll() {
        List<ProviderSettingsDto> settings = service.getAll().stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntityFactory.ok(settings);
    }
}
