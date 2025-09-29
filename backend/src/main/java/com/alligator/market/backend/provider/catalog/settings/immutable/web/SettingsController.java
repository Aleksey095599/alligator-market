package com.alligator.market.backend.provider.catalog.settings.immutable.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.provider.catalog.settings.immutable.service.SettingsUseCase;
import com.alligator.market.backend.provider.catalog.settings.immutable.web.dto.SettingsDto;
import com.alligator.market.backend.provider.catalog.settings.immutable.web.dto.SettingsDtoMapper;
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
public class SettingsController {

    private final SettingsUseCase service;
    private final SettingsDtoMapper mapper;

    /** Вернуть все настройки. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<SettingsDto>>> getAll() {
        List<SettingsDto> settings = service.getAll().stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntityFactory.ok(settings);
    }
}
