package com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.controller;

import com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.dto.FxSpotListItemResponse;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.mapper.FxSpotListItemResponseMapper;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.list.FxSpotListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST API-адаптер use case чтения списка инструментов FOREX_SPOT.
 */
@RestController
@RequestMapping("/api/v1/fx-spot")
public class FxSpotListController {

    private final FxSpotListService fxSpotListService;

    public FxSpotListController(FxSpotListService fxSpotListService) {
        this.fxSpotListService = fxSpotListService;
    }

    @GetMapping
    public ResponseEntity<List<FxSpotListItemResponse>> getAll() {
        List<FxSpotListItemResponse> list = fxSpotListService.findAll().stream()
                .map(FxSpotListItemResponseMapper::toResponse)
                .toList();

        return ResponseEntity.ok(list);
    }
}
