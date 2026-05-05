package com.alligator.market.backend.marketdata.capture.process.passport.api.query.list.controller;

import com.alligator.market.backend.marketdata.capture.process.passport.api.query.list.dto.MarketDataCaptureProcessPassportListItemResponse;
import com.alligator.market.backend.marketdata.capture.process.passport.api.query.list.mapper.MarketDataCaptureProcessPassportListItemResponseMapper;
import com.alligator.market.backend.marketdata.capture.process.passport.application.query.list.MarketDataCaptureProcessPassportListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер каталога паспортов процессов захвата рыночных данных.
 */
@RestController
@RequestMapping("/api/v1/market-data-capture-processes")
@RequiredArgsConstructor
public class MarketDataCaptureProcessPassportListController {

    private final MarketDataCaptureProcessPassportListService service;

    /**
     * Вернуть все паспорта процессов захвата.
     */
    @GetMapping
    public ResponseEntity<List<MarketDataCaptureProcessPassportListItemResponse>> getAll() {
        var passports = service.findAll();

        List<MarketDataCaptureProcessPassportListItemResponse> list = passports.entrySet().stream()
                .map(entry -> MarketDataCaptureProcessPassportListItemResponseMapper.toResponse(entry.getKey(), entry.getValue()))
                .toList();
        return ResponseEntity.ok(list);
    }
}
