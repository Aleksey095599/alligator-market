package com.alligator.market.backend.provider.catalog.passport.web;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.provider.catalog.passport.service.PassportCatalogService;
import com.alligator.market.backend.provider.catalog.passport.web.dto.out.PassportResponseDto;
import com.alligator.market.backend.provider.catalog.passport.web.dto.mapper.PassportDtoMapper;
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
public class PassportController {

    private final PassportCatalogService service;

    /**
     * Вернуть все паспорта провайдеров.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PassportResponseDto>>> getAll() {
        List<PassportResponseDto> list = service.findAll().stream()
                .map(PassportDtoMapper::toProviderPassportResponseDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }
}
