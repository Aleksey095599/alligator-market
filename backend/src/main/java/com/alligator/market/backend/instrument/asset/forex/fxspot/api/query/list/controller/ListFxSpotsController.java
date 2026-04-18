package com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.controller;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.dto.FxSpotListResponse;
import com.alligator.market.backend.instrument.asset.forex.fxspot.api.query.list.mapper.FxSpotListResponseMapper;
import com.alligator.market.backend.instrument.asset.forex.fxspot.application.query.list.ListFxSpotsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API-контроллер use case получения списка инструментов FOREX_SPOT.
 */
@RestController
@RequestMapping("/api/v1/fx-spot")
@RequiredArgsConstructor
public class ListFxSpotsController {

    private final ListFxSpotsService listFxSpotsService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FxSpotListResponse>>> findAll() {
        List<FxSpotListResponse> list = listFxSpotsService.findAll().stream()
                .map(FxSpotListResponseMapper::toResponse)
                .toList();

        return ResponseEntityFactory.ok(list);
    }
}
