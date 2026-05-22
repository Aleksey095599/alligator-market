package com.alligator.market.backend.instrument.api.query.catalog.controller;

import com.alligator.market.backend.instrument.api.query.catalog.dto.InstrumentCatalogItemResponse;
import com.alligator.market.backend.instrument.api.query.catalog.mapper.InstrumentCatalogItemResponseMapper;
import com.alligator.market.backend.instrument.application.catalog.InstrumentCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/instruments/catalog")
@RequiredArgsConstructor
public class InstrumentCatalogController {
    private final InstrumentCatalogService service;

    @GetMapping
    public ResponseEntity<List<InstrumentCatalogItemResponse>> getAll() {
        var catalogItems = service.findAll();

        List<InstrumentCatalogItemResponse> list = catalogItems.stream()
                .map(InstrumentCatalogItemResponseMapper::toResponse)
                .toList();

        return ResponseEntity.ok(list);
    }
}
