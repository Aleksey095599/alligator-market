package com.alligator.market.backend.providers.service;

import com.alligator.market.backend.providers.dto.ProviderCreateDto;
import com.alligator.market.backend.providers.dto.ProviderDto;
import com.alligator.market.backend.providers.dto.ProviderUpdateDto;

import java.util.List;

/**
 * Интерфейс сервиса для операций с провайдерами.
 */
public interface ProviderService {

    String createProvider(ProviderCreateDto dto);

    void updateProvider(String name, ProviderUpdateDto dto);

    void deleteProvider(String name);

    List<ProviderDto> findAll();
}
