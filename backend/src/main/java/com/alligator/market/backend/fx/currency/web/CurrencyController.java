package com.alligator.market.backend.fx.currency.web;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
import com.alligator.market.backend.fx.currency.dto.CurrencyDto;
import com.alligator.market.backend.fx.currency.dto.CurrencyUpdateDto;
import com.alligator.market.backend.fx.currency.service.CurrencyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST-контроллер для операций с валютами.
 */
@RestController
@RequestMapping("/api/v1/currencies")
@RequiredArgsConstructor
@Slf4j
public class CurrencyController {

    private final CurrencyService service;

    //=====================
    // Создать новую валюту
    //=====================
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid CurrencyDto dto) {

        String code = service.createCurrency(dto);
        URI location = ServletUriComponentsBuilder // Формируем ссылку на созданный ресурс
                .fromCurrentRequest()
                .path("/{code}")
                .buildAndExpand(code)
                .toUri();
        return ResponseEntityFactory.created(location, code);
    }

    //=================
    // Обновить валюту
    //=================
    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable String code,
            @RequestBody @Valid CurrencyUpdateDto dto) {
        service.updateCurrency(code, dto);
        return ResponseEntityFactory.ok(null);
    }

    //===============
    // Удалить валюту
    //===============
    @DeleteMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String code) {

        service.deleteCurrency(code);
        return ResponseEntityFactory.ok(null);
    }

    //===================
    // Вернуть все валюты
    //===================
    @GetMapping
    public ResponseEntity<ApiResponse<List<CurrencyDto>>> getAll() {

        return ResponseEntityFactory.ok(service.findAll());
    }

}