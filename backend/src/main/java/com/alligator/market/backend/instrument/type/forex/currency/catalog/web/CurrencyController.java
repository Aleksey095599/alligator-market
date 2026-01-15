package com.alligator.market.backend.instrument.type.forex.currency.catalog.web;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.service.CurrencyUseCase;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.in.CurrencyUpdateDto;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.common.CurrencyDto;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.mapper.CurrencyDtoMapper;
import com.alligator.market.domain.instrument.type.forex.currency.model.Currency;
import com.alligator.market.domain.instrument.type.forex.currency.code.CurrencyCode;
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

    /**
     * Создать валюту.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid CurrencyDto dto) {

        Currency created = service.create(CurrencyDtoMapper.toDomainCreate(dto));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(created.code().value())
                .toUri();
        return ResponseEntityFactory.created(location, created.code().value());
    }

    /**
     * Обновить валюту.
     */
    @PutMapping("/{code}")
    public ResponseEntity<Void> update(
            @PathVariable @Pattern(regexp = "^[A-Z]{3}$") String code,
            @RequestBody @Valid CurrencyUpdateDto dto) {

        service.update(CurrencyDtoMapper.toDomainUpdate(code, dto));
        return ResponseEntityFactory.noContent();
    }

    /**
     * Удалить валюту.
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(
            @PathVariable @Pattern(regexp = "^[A-Z]{3}$") String code) {

        service.delete(CurrencyCode.of(code));
        return ResponseEntityFactory.noContent();
    }

    /**
     * Вернуть все валюты.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CurrencyDto>>> getAll() {

        List<CurrencyDto> currencyDtoList = service.findAll()
                .stream()
                .map(CurrencyDtoMapper::toResponseDto)
                .toList();
        return ResponseEntityFactory.ok(currencyDtoList);
    }
}
