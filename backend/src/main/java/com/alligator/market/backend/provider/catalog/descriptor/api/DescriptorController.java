package com.alligator.market.backend.provider.catalog.descriptor.api;

import com.alligator.market.backend.provider.catalog.descriptor.service.DescriptorUseCase;
import com.alligator.market.backend.provider.catalog.descriptor.api.dto.DescriptorDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер дескрипторов.
 */
@RestController
@RequestMapping("/api/v1/providers")
@RequiredArgsConstructor
public class DescriptorController {

    private final DescriptorUseCase service;
    private final DescriptorDtoMapper mapper;

    /** Вернуть все дескрипторы. */

}
