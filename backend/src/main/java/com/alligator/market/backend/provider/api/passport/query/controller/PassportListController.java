package com.alligator.market.backend.provider.api.passport.query.controller;

import com.alligator.market.backend.provider.application.passport.catalog.PassportCatalogService;
import com.alligator.market.backend.provider.api.passport.query.dto.PassportListItemResponse;
import com.alligator.market.backend.provider.api.passport.query.mapper.PassportListItemResponseMapper;
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
public class PassportListController {

    private final PassportCatalogService service;

    /**
     * Вернуть все паспорта провайдеров.
     */
    @GetMapping
    public ResponseEntity<List<PassportListItemResponse>> getAll() {
        var passports = service.findAll();

        List<PassportListItemResponse> list = passports.entrySet().stream()
                .map(entry -> PassportListItemResponseMapper.toProviderPassportResponseDto(entry.getKey(), entry.getValue()))
                .toList();
        return ResponseEntity.ok(list);
    }
}
