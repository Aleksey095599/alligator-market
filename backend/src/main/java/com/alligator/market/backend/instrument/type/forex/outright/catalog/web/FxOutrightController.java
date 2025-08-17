package com.alligator.market.backend.instrument.type.forex.outright.catalog.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.service.FxOutrightService;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.web.dto.FxOutrightDto;
import com.alligator.market.backend.instrument.type.forex.outright.catalog.web.mapper.FxOutrightDtoMapper;
import com.alligator.market.domain.instrument.type.forex.outright.model.FxOutright;
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
@RequestMapping("/api/v1/fx-outrights")
@RequiredArgsConstructor
@Slf4j
public class FxOutrightController {

    private final FxOutrightService service;
    private final FxOutrightDtoMapper mapper;

    /** Создать инструмент. */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid FxOutrightDto dto) {
        FxOutright model = mapper.toDomain(dto);
        String code = service.create(model);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(code)
                .toUri();
        return ResponseEntityFactory.created(location, code);
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
