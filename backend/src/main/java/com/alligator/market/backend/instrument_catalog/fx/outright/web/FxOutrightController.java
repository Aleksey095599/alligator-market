package com.alligator.market.backend.instrument_catalog.fx.outright.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument_catalog.fx.outright.service.FxOutrightService;
import com.alligator.market.backend.instrument_catalog.fx.outright.web.dto.FxOutrightDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер инструментов FX OUTRIGHT.
 */
@RestController
@RequestMapping("/api/v1/fx-outright-instrument")
@RequiredArgsConstructor
@Slf4j
public class FxOutrightController {

    private final FxOutrightService service;

    /** Вернуть все инструменты FX OUTRIGHT. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FxOutrightDto>>> getAll() {

        // Извлекаем сервисом список инструментов и преобразуем к DTO
        List<FxOutrightDto> dtoList = service.findAll()
                .stream()
                .map(s -> new FxOutrightDto(
                        s.internalCode(),
                        s.currencyPair().pairCode(),
                        s.valueDateCode()
                ))
                .toList();

        return ResponseEntityFactory.ok(dtoList);
    }
}
