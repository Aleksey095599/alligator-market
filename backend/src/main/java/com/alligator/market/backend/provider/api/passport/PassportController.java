package com.alligator.market.backend.provider.api.passport;

import com.alligator.market.backend.provider.application.passport.catalog.PassportCatalogService;
import com.alligator.market.backend.provider.api.passport.dto.PassportResponseDto;
import com.alligator.market.backend.provider.api.passport.mapper.PassportDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер каталога паспортов провайдеров рыночных данных.
 */
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class PassportController {

    private final PassportCatalogService service;

    /**
     * Вернуть все паспорта провайдеров.
     */
    @GetMapping
    public ResponseEntity<List<PassportResponseDto>> getAll() {
        var passports = service.findAll();

        List<PassportResponseDto> list = passports.entrySet().stream()
                .map(entry -> PassportDtoMapper.toProviderPassportResponseDto(entry.getKey(), entry.getValue()))
                .toList();
        return ResponseEntity.ok(list);
    }
}
