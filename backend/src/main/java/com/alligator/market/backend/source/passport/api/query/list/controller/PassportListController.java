package com.alligator.market.backend.source.passport.api.query.list.controller;

import com.alligator.market.backend.source.passport.application.query.list.PassportListService;
import com.alligator.market.backend.source.passport.api.query.list.dto.PassportListItemResponse;
import com.alligator.market.backend.source.passport.api.query.list.mapper.PassportListItemResponseMapper;
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

    private final PassportListService service;

    /**
     * Вернуть все паспорта провайдеров.
     */
    @GetMapping
    public ResponseEntity<List<PassportListItemResponse>> getAll() {
        var passports = service.findAll();

        List<PassportListItemResponse> list = passports.stream()
                .map(PassportListItemResponseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }
}
