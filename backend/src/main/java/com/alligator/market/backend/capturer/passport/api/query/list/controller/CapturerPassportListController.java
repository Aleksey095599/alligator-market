package com.alligator.market.backend.capturer.passport.api.query.list.controller;

import com.alligator.market.backend.capturer.passport.api.query.list.dto.CapturerPassportListItemResponse;
import com.alligator.market.backend.capturer.passport.api.query.list.mapper.CapturerPassportListItemResponseMapper;
import com.alligator.market.backend.capturer.passport.application.query.list.CapturerPassportListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/capturer-passports")
@RequiredArgsConstructor
public class CapturerPassportListController {
    private final CapturerPassportListService service;

    @GetMapping
    public ResponseEntity<List<CapturerPassportListItemResponse>> getAll() {
        var passports = service.findAll();

        List<CapturerPassportListItemResponse> list = passports.stream()
                .map(CapturerPassportListItemResponseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }
}
