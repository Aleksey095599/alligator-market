package com.alligator.market.backend.instrument.type.forex.spot.catalog.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.service.FxSpotAssembler;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.service.FxSpotUseCase;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.in.FxSpotDto;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.in.FxSpotUpdateDto;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.mapper.FxSpotDtoMapper;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.web.dto.out.FxSpotListItemDto;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import jakarta.validation.Valid;
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

    private final FxSpotUseCase service;
    private final FxSpotDtoMapper mapper;
    private final FxSpotAssembler assembler;

    /** Создать инструмент. */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid FxSpotDto dto) {
        // DTO → модель
        FxSpot fxSpot = assembler.toDomain(dto);
        // Передаем модель сервису
        String code = service.create(fxSpot);
        String symbol = fxSpot.symbol();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(code)
                .toUri();
        return ResponseEntityFactory.created(location, symbol);
    }

    /** Обновить инструмент. */
    @PatchMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable String code,
                                                    @RequestBody @Valid FxSpotUpdateDto dto) {
        // код и DTO → модель
        FxSpot fxSpot = assembler.toDomainByCode(code, dto);
        // Передаем модель сервису
        service.update(fxSpot);
        return ResponseEntityFactory.ok(null);
    }

    /** Удалить инструмент. */
    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String code) {
        service.delete(code);
        return ResponseEntityFactory.ok(null);
    }

    /** Вернуть все инструменты. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FxSpotListItemDto>>> getAll() {
        List<FxSpotListItemDto> list = service.findAll().stream()
                .map(mapper::toListItemDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }
}
