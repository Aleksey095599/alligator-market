package com.alligator.market.backend.instrument_catalog.fx.spot.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument_catalog.fx.spot.service.FxSpotInstrumentService;
import com.alligator.market.backend.instrument_catalog.fx.spot.web.dto.FxSpotDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер инструментов FX SPOT.
 */
@RestController
@RequestMapping("/api/v1/fx-spots")
@RequiredArgsConstructor
@Slf4j
public class FxSpotController {

    private final FxSpotInstrumentService service;

    /** Вернуть все инструменты FX SPOT. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FxSpotDto>>> getAll() {

        // Извлекаем сервисом список инструментов и преобразуем к DTO
        List<FxSpotDto> dtoList = service.findAll()
                .stream()
                .map(s -> new FxSpotDto(
                        s.internalCode(),
                        s.currencyPair().pairCode(),
                        s.valueDateCode()
                ))
                .toList();

        return ResponseEntityFactory.ok(dtoList);
    }
}
