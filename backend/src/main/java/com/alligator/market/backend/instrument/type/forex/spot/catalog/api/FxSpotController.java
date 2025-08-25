package com.alligator.market.backend.instrument.type.forex.spot.catalog.api;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.service.FxSpotService;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.api.dto.FxSpotDto;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.api.dto.FxSpotUpdateDto;
import com.alligator.market.backend.instrument.type.forex.spot.catalog.api.dto.FxSpotDtoMapper;
import com.alligator.market.domain.instrument.type.forex.spot.model.FxSpot;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FxSpotController {

    private final FxSpotService service;
    private final FxSpotDtoMapper mapper;

    /** Создать инструмент. */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid FxSpotDto dto) {
        FxSpot model = mapper.toDomain(dto);
        String code = service.create(model);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(code)
                .toUri();
        return ResponseEntityFactory.created(location, code);
    }

    /** Обновить точность котировки. */
    @PatchMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> updateQuoteDecimal(@PathVariable String code,
                                                                @RequestBody @Valid FxSpotUpdateDto dto) {
        service.updateQuoteDecimal(code, dto.quoteDecimal());
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
    public ResponseEntity<ApiResponse<List<FxSpotDto>>> getAll() {
        List<FxSpotDto> list = service.findAll().stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }
}
