package com.alligator.market.backend.instrument.asset.forex.spot.catalog.web;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.instrument.asset.forex.spot.catalog.service.FxSpotDomainFactory;
import com.alligator.market.backend.instrument.asset.forex.spot.catalog.service.FxSpotCatalogService;
import com.alligator.market.backend.instrument.asset.forex.spot.catalog.web.dto.in.FxSpotCreateDto;
import com.alligator.market.backend.instrument.asset.forex.spot.catalog.web.dto.in.FxSpotUpdateDto;
import com.alligator.market.backend.instrument.asset.forex.spot.catalog.web.dto.mapper.FxSpotDtoMapper;
import com.alligator.market.backend.instrument.asset.forex.spot.catalog.web.dto.out.FxSpotResponseDto;
import com.alligator.market.domain.instrument.vo.InstrumentCode;
import com.alligator.market.domain.instrument.market.forex.spot.model.FxSpot;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST-контроллер для инструментов FX_SPOT.
 */
@RestController
@RequestMapping("/api/v1/fx-spot")
@RequiredArgsConstructor
public class FxSpotController {

    private final FxSpotCatalogService service;
    private final FxSpotDomainFactory factory;

    /**
     * Создать инструмент.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody FxSpotCreateDto dto) {

        FxSpot created = service.create(factory.fromCreateDto(dto));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{instrumentCode}")
                .buildAndExpand(created.instrumentCode().value())
                .toUri();
        return ResponseEntityFactory.created(location, created.instrumentCode().value());
    }

    /**
     * Обновить инструмент.
     */
    @PatchMapping("/{instrumentCode}")
    public ResponseEntity<Void> update(@PathVariable String instrumentCode,
                                       @RequestBody FxSpotUpdateDto dto) {

        // Парсим строковый код инструмента в объект-значение
        InstrumentCode code = InstrumentCode.of(instrumentCode);
        service.update(factory.fromUpdateDto(code, dto));
        return ResponseEntityFactory.noContent();
    }

    /**
     * Удалить инструмент.
     */
    @DeleteMapping("/{instrumentCode}")
    public ResponseEntity<Void> delete(@PathVariable String instrumentCode) {

        // Парсим строковый код инструмента в объект-значение
        InstrumentCode code = InstrumentCode.of(instrumentCode);
        service.delete(code);
        return ResponseEntityFactory.noContent();
    }

    /**
     * Вернуть все инструменты.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FxSpotResponseDto>>> getAll() {

        List<FxSpotResponseDto> list = service.findAll().stream()
                .map(FxSpotDtoMapper::toFxSpotResponseDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }
}
