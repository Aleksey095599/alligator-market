package com.alligator.market.backend.provider.profile.catalog.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.provider.profile.catalog.web.dto.ProviderProfileDto;
import com.alligator.market.backend.provider.profile.catalog.web.dto.ProviderProfileStatusDto;
import com.alligator.market.backend.provider.profile.catalog.service.ProviderProfileService;
import com.alligator.market.backend.provider.profile.catalog.web.mapper.ProviderProfileDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер профилей провайдеров рыночных данных.
 */
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderProfileController {

    private final ProviderProfileService service;
    private final ProviderProfileDtoMapper mapper;

    //==============================================
    //                  Операции
    //==============================================

    /** Вернуть все активные профили. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProviderProfileDto>>> getAll() {
        List<ProviderProfileDto> list = service.findAllActive().values().stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }

    /** Вернуть все профили со статусами. */
    @GetMapping("/audit")
    public ResponseEntity<ApiResponse<List<ProviderProfileStatusDto>>> getAllWithStatus() {
        List<ProviderProfileStatusDto> list = service.findAllWithStatus().entrySet().stream()
                .map(e -> mapper.toStatusDto(e.getKey(), e.getValue()))
                .toList();
        return ResponseEntityFactory.ok(list);
    }

}
