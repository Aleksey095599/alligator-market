package com.alligator.market.backend.provider.profile.catalog.api;

import com.alligator.market.backend.common.web.ApiResponse;
import com.alligator.market.backend.common.web.ResponseEntityFactory;
import com.alligator.market.backend.provider.profile.catalog.api.dto.ProfileDto;
import com.alligator.market.backend.provider.profile.catalog.service.crud.ProviderProfileService;
import com.alligator.market.backend.provider.profile.catalog.api.dto.ProfileDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер профилей провайдеров.
 */
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class ProfileController {

    private final ProviderProfileService service;
    private final ProfileDtoMapper mapper;

    /** Вернуть все активные профили. */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProfileDto>>> getAll() {
        List<ProfileDto> list = service.findAllActive().values().stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntityFactory.ok(list);
    }

    /** Вернуть все профили со статусами. */
    @GetMapping("/audit")
    public ResponseEntity<ApiResponse<List<ProviderProfileStatusDto>>> getAllWithStatus() {
        List<ProviderProfileStatusDto> list = service.findAllWithStatus().entrySet().stream()
                .map(e -> mapper.toStatusDto(e.getKey(), e.getValue()))
                .toList();
        return ResponseEntityFactory.ok(list);
    }
}
