package com.alligator.market.backend.provider.catalog.web;

import com.alligator.market.backend.common.web.http.ApiResponse;
import com.alligator.market.backend.common.web.http.ResponseEntityFactory;
import com.alligator.market.backend.provider.catalog.service.ProviderCatalogUseCase;
import com.alligator.market.backend.provider.catalog.web.dto.out.ProviderResponseDto;
import com.alligator.market.backend.provider.catalog.web.dto.mapper.ProviderDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер каталога провайдеров рыночных данных.
 */
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderCatalogUseCase service;

    /**
     * Вернуть все дескрипторы провайдеров.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProviderResponseDto>>> getAll() {
        List<ProviderResponseDto> list = service.findAll().stream()
                .map(ProviderDtoMapper::toDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }
}
