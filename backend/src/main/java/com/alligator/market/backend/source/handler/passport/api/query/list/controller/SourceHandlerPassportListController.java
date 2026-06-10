package com.alligator.market.backend.source.handler.passport.api.query.list.controller;

import com.alligator.market.backend.source.handler.passport.api.query.list.dto.SourceHandlerPassportListItemResponse;
import com.alligator.market.backend.source.handler.passport.api.query.list.mapper.SourceHandlerPassportListItemResponseMapper;
import com.alligator.market.backend.source.handler.passport.application.query.list.SourceHandlerPassportListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/source-handler-passports")
@RequiredArgsConstructor
public class SourceHandlerPassportListController {
    private final SourceHandlerPassportListService service;

    @GetMapping
    public ResponseEntity<List<SourceHandlerPassportListItemResponse>> getAll() {
        var passports = service.findAll();

        List<SourceHandlerPassportListItemResponse> list = passports.stream()
                .map(SourceHandlerPassportListItemResponseMapper::toResponse)
                .toList();
        return ResponseEntity.ok(list);
    }
}
