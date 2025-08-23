package com.alligator.market.backend.instrument.type.forex.outright.catalog.api;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.service.crud.FxOutrightService;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.api.dto.FxOutrightDto;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.api.dto.FxOutrightUpdateDto;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.api.dto.FxOutrightDtoMapper;
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
 * REST-контроллер для инструментов FX_OUTRIGHT.
 */
@RestController
@RequestMapping("/api/v1/fx-outright")
@RequiredArgsConstructor
@Slf4j
public class FxOutrightController {

    private final FxOutrightService service;
    private final FxOutrightDtoMapper mapper;

    /** Создать инструмент. */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid FxOutrightDto dto) {
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
                                                                @RequestBody @Valid FxOutrightUpdateDto dto) {
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
    public ResponseEntity<ApiResponse<List<FxOutrightDto>>> getAll() {
        List<FxOutrightDto> list = service.findAll().stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }
}
