package com.alligator.market.backend.quotes.stream.settings.web;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
import com.alligator.market.backend.quotes.stream.settings.dto.SettingsCreateDto;
import com.alligator.market.backend.quotes.stream.settings.dto.SettingsDto;
import com.alligator.market.backend.quotes.stream.settings.dto.SettingsUpdateDto;
import com.alligator.market.backend.quotes.stream.settings.service.SettingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST-контроллер для операций с соответсвующее таблицей.
 */
@RestController
@RequestMapping("/api/v1/streaming-configs")
@RequiredArgsConstructor
@Slf4j
public class FxPairStreamingConfigController {

    private final SettingsService service;

    //========================
    // Создать новые настройки
    //========================
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid SettingsCreateDto dto) {

        String id = service.createConfig(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{pair}/{provider}/{mode}")
                .buildAndExpand(dto.pair(), dto.provider(), dto.mode())
                .toUri();
        return ResponseEntityFactory.created(location, id);
    }

    //===================
    // Обновить настройки
    //===================
    @PutMapping("/{pair}/{provider}/{mode}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable String pair,
            @PathVariable String provider,
            @PathVariable String mode,
            @RequestBody @Valid SettingsUpdateDto dto) {
        service.updateConfig(pair, provider, mode, dto);
        return ResponseEntityFactory.ok(null);
    }

    //==================
    // Удалить настройки
    //==================
    @DeleteMapping("/{pair}/{provider}/{mode}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable String pair,
            @PathVariable String provider,
            @PathVariable String mode) {
        service.deleteConfig(pair, provider, mode);
        return ResponseEntityFactory.ok(null);
    }

    //======================
    // Вернуть все настройки
    //======================
    @GetMapping
    public ResponseEntity<ApiResponse<List<SettingsDto>>> getAll() {

        return ResponseEntityFactory.ok(service.findAll());
    }
}
