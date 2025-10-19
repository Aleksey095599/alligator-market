package com.alligator.market.backend.instrument.type.forex.spot.catalog.web;

import com.alligator.market.backend.common.web.http.ApiResponse;
import com.alligator.market.backend.common.web.http.ResponseEntityFactory;
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
    private final FxSpotAssembler assembler;

    /** Создать инструмент. */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid FxSpotDto dto) {

        FxSpot created = service.create(assembler.toDomain(dto));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{instrumentCode}")
                .buildAndExpand(created.instrumentCode())
                .toUri();
        return ResponseEntityFactory.created(location, created.instrumentSymbol()); // Умышленно символ, а не код
    }

    /** Обновить инструмент. */
    @PatchMapping("/{instrumentCode}")
    public ResponseEntity<ApiResponse<Void>> update(@PathVariable String instrumentCode,
                                                    @RequestBody @Valid FxSpotUpdateDto dto) {

        service.update(assembler.toDomainByCode(instrumentCode, dto));
        return ResponseEntityFactory.noContent();
    }

    /** Удалить инструмент. */
    @DeleteMapping("/{instrumentCode}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String instrumentCode) {

        service.delete(instrumentCode);
        return ResponseEntityFactory.noContent();
    }

    /** Вернуть все инструменты. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FxSpotListItemDto>>> getAll() {

        List<FxSpotListItemDto> list = service.findAll().stream()
                .map(FxSpotDtoMapper::toListItemDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }
}
