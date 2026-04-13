package com.alligator.market.backend.instrument.asset.forex.reference.currency.api;

import com.alligator.market.backend.common.web.response.ApiResponse;
import com.alligator.market.backend.common.web.response.ResponseEntityFactory;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.dto.common.CurrencyDto;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.dto.in.CurrencyUpdateDto;
import com.alligator.market.backend.instrument.asset.forex.reference.currency.api.dto.mapper.CurrencyDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * Обновить валюту.
     */
    @PutMapping("/{code}")
    public ResponseEntity<Void> update(@PathVariable String code, @RequestBody CurrencyUpdateDto dto) {

        service.update(CurrencyDtoMapper.toDomainUpdate(code, dto));
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
