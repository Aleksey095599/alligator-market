package com.alligator.market.backend.marketdata.capture.process.api.passport.query.list.controller;

import com.alligator.market.backend.marketdata.capture.process.api.passport.query.list.dto.CaptureProcessPassportListItemResponse;
import com.alligator.market.backend.marketdata.capture.process.api.passport.query.list.mapper.CaptureProcessPassportListItemResponseMapper;
import com.alligator.market.backend.marketdata.capture.process.application.passport.query.list.CaptureProcessPassportListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер каталога паспортов процессов фиксации рыночных данных.
 */
@RestController
@RequestMapping("/api/v1/market-data-capture-processes")
@RequiredArgsConstructor
public class CaptureProcessPassportListController {

    private final CaptureProcessPassportListService service;

    /**
     * Вернуть все паспорта процессов фиксации.
     */
    @GetMapping
    public ResponseEntity<List<CaptureProcessPassportListItemResponse>> getAll() {
        var passports = service.findAll();

        List<CaptureProcessPassportListItemResponse> list = passports.entrySet().stream()
                .map(entry -> CaptureProcessPassportListItemResponseMapper.toResponse(entry.getKey(), entry.getValue()))
                .toList();
        return ResponseEntity.ok(list);
    }
}
