package com.alligator.market.backend.quotes.stream.ccypair_feed_settings.web;

import com.alligator.market.backend.common.web.dto.ApiResponse;
import com.alligator.market.backend.common.web.util.ResponseEntityFactory;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.dto.FeedSettingsCreateDto;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.dto.FeedSettingsDto;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.dto.FeedSettingsUpdateDto;
import com.alligator.market.backend.quotes.stream.ccypair_feed_settings.service.FeedSettingsService;
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
public class FeedSettingsController {

    private final FeedSettingsService service;

    //========================
    // Создать новые настройки
    //========================
    @PostMapping
    public ResponseEntity<ApiResponse<String>> create(@RequestBody @Valid FeedSettingsCreateDto dto) {

        String id = service.createSettings(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{pair}/{provider}")
                .buildAndExpand(dto.pair(), dto.provider())
                .toUri();
        return ResponseEntityFactory.created(location, id);
    }

    //===================
    // Обновить настройки
    //===================
    @PutMapping("/{pair}/{provider}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable String pair,
            @PathVariable String provider,
            @RequestBody @Valid FeedSettingsUpdateDto dto) {
        service.updateSettings(pair, provider, dto);
        return ResponseEntityFactory.ok(null);
    }

    //==================
    // Удалить настройки
    //==================
    @DeleteMapping("/{pair}/{provider}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable String pair,
            @PathVariable String provider) {
        service.deleteSettings(pair, provider);
        return ResponseEntityFactory.ok(null);
    }

    //======================
    // Вернуть все настройки
    //======================
    @GetMapping
    public ResponseEntity<ApiResponse<List<FeedSettingsDto>>> getAll() {

        return ResponseEntityFactory.ok(service.findAll());
    }
}
