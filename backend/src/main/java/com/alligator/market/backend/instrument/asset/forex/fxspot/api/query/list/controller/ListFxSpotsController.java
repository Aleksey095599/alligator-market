package com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.controller;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.dto.FxSpotListItemResponse;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.mapper.FxSpotListItemResponseMapper;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.list.ListFxSpotsService;
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
public class ListFxSpotsController {

    private final ListFxSpotsService listFxSpotsService;

    public ListFxSpotsController(ListFxSpotsService listFxSpotsService) {
        this.listFxSpotsService = listFxSpotsService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FxSpotListItemResponse>>> getAll() {
        List<FxSpotListItemResponse> list = listFxSpotsService.findAll().stream()
                .map(FxSpotListItemResponseMapper::toResponse)
                .toList();

        return ResponseEntityFactory.ok(list);
    }
}
