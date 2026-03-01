package com.alligator.market.backend.provider.catalog.passport.service;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.readmodel.passport.query.port.ProviderPassportQueryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Реализация сервиса {@link PassportCatalogService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PassportCatalogServiceImpl implements PassportCatalogService {

    /* Query-порт чтения паспортов провайдеров из read model (provider_passport). */
    private final ProviderPassportQueryPort queryPort;

    @Override
    @Transactional(readOnly = true)
    public List<ProviderPassport> findAll() {
        Map<ProviderCode, ProviderPassport> passports = queryPort.findAll();

        // Сохраняем порядок итерации карты (если адаптер возвращает LinkedHashMap с ORDER BY в SQL).
        List<ProviderPassport> result = List.copyOf(passports.values());

        log.debug("Found {} provider passports", result.size());
        return result;
    }
}
