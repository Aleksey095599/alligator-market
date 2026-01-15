package com.alligator.market.backend.provider.catalog.passport.web;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.provider.catalog.passport.service.ProviderPassportUseCase;
import com.alligator.market.backend.provider.catalog.passport.web.dto.out.ProviderPassportResponseDto;
import com.alligator.market.backend.provider.catalog.passport.web.dto.mapper.ProviderPassportDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер каталога паспортов провайдеров рыночных данных.
 */
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProviderController {

    private final ProviderPassportUseCase service;

    /**
     * Вернуть все паспорта провайдеров.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProviderPassportResponseDto>>> getAll() {
        List<ProviderPassportResponseDto> list = service.findAll().stream()
                .map(ProviderPassportDtoMapper::toProviderPassportResponseDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }
}
