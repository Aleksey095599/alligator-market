package com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web.dto.common.CurrencyDto;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web.dto.in.UpdateCurrencyDto;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.web.dto.mapper.CurrencyDtoMapper;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.Currency;
import com.alligator.market.backend.instrument.type.forex.ref.currency.catalog.service.CurrencyUseCase;
import com.alligator.market.domain.instrument.type.forex.ref.currency.model.CurrencyCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST-контроллер для валют.
 */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
@Validated
public class CurrencyController {

    private final CurrencyUseCase service;
    private final CurrencyDtoMapper mapper;

    /** Создать валюту. */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid CurrencyDto dto) {

        Currency created = service.create(mapper.toDomain(dto));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(created.code().value())
                .toUri();
        return ResponseEntityFactory.created(location, created.code().value());
    }

    /** Обновить валюту. */
    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable @Pattern(regexp = "^[A-Z]{3}$") String code,
            @RequestBody @Valid UpdateCurrencyDto dto) {

        service.update(mapper.toDomain(code, dto));
        return ResponseEntity.noContent().build();
    }

    /** Удалить валюту. */
    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable @Pattern(regexp = "^[A-Z]{3}$") String code) {

        service.delete(CurrencyCode.of(code));
        return ResponseEntityFactory.ok(null);
    }

    /** Вернуть все валюты. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CurrencyDto>>> getAll() {

        List<CurrencyDto> currencyDtoList = service.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntityFactory.ok(currencyDtoList);
    }
}
