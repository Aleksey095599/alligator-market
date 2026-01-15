package com.alligator.market.backend.provider.catalog.passport.service;

import com.alligator.market.backend.provider.catalog.passport.persistence.jpa.PassportEntityMapper;
import com.alligator.market.backend.provider.catalog.passport.persistence.jpa.PassportJpaRepository;
import com.alligator.market.domain.provider.contract.passport.ProviderPassport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Реализация сервиса {@link PassportCatalogService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PassportCatalogServiceImpl implements PassportCatalogService {

    /* JPA-репозиторий паспортов провайдеров. */
    private final PassportJpaRepository passportJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProviderPassport> findAll() {
        List<ProviderPassport> result = passportJpaRepository.findAll().stream()
                .map(PassportEntityMapper::toDomain)
                .toList();
        log.debug("Found {} provider passports", result.size());
        return result;
    }
}
