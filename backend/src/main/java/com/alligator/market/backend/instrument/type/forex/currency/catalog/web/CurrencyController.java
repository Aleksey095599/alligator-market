package com.alligator.market.backend.instrument.type.forex.currency.catalog.web;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.service.CurrencyCatalogService;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.common.CurrencyDto;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.in.CurrencyUpdateDto;
import com.alligator.market.backend.instrument.type.forex.currency.catalog.web.dto.mapper.CurrencyDtoMapper;
import com.alligator.market.domain.instrument.type.forex.currency.vo.CurrencyCode;
import com.alligator.market.domain.instrument.type.forex.currency.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
public class CurrencyController {

    private final CurrencyCatalogService service;

    /**
     * Создать валюту.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody CurrencyDto dto) {

        Currency created = service.create(CurrencyDtoMapper.toDomain(dto));
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
    public ResponseEntity<Void> update(@PathVariable String code, @RequestBody CurrencyUpdateDto dto) {

        service.update(CurrencyDtoMapper.toDomainUpdate(code, dto));
        return ResponseEntityFactory.noContent();
    }

    /**
     * Удалить валюту.
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {

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
                .map(CurrencyDtoMapper::toCurrencyDto)
                .toList();
        return ResponseEntityFactory.ok(currencyDtoList);
    }
}
