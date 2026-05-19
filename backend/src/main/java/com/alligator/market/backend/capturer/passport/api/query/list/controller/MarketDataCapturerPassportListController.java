package com.alligator.market.backend.capturer.passport.api.query.list.controller;

import com.alligator.market.backend.capturer.passport.api.query.list.dto.MarketDataCapturerPassportListItemResponse;
import com.alligator.market.backend.capturer.passport.api.query.list.mapper.MarketDataCapturerPassportListItemResponseMapper;
import com.alligator.market.backend.capturer.passport.application.query.list.MarketDataCapturerPassportListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/capturers")
@RequiredArgsConstructor
public class MarketDataCapturerPassportListController {
    private final MarketDataCapturerPassportListService service;

    @GetMapping
    public ResponseEntity<List<MarketDataCapturerPassportListItemResponse>> getAll() {
        var passports = service.findAll();

        List<MarketDataCapturerPassportListItemResponse> list = passports.stream()
                .map(MarketDataCapturerPassportListItemResponseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }
}
