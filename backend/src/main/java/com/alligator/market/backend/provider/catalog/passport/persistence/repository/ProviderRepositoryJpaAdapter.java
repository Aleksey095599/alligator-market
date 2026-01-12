package com.alligator.market.backend.provider.catalog.passport.persistence.repository;

import com.alligator.market.backend.provider.catalog.passport.persistence.jpa.ProviderJpaRepository;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.repository.ProviderRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Адаптер, реализующий доменный порт репозитория провайдеров через Spring Data JPA.
 */
@Repository
@Transactional(readOnly = true) // <-- Все операции только на чтение
public class ProviderRepositoryJpaAdapter implements ProviderRepository {

    /* JPA-репозиторий. */
    private final ProviderJpaRepository jpa;

    /* Конструктор. */
    public ProviderRepositoryJpaAdapter(ProviderJpaRepository jpa) {
        this.jpa = jpa;
    }

    /**
     * Найти все коды провайдеров (натуральные ключи).
     */
    @Override
    public List<ProviderCode> findAllCodes() {
        return jpa.findAllCodes().stream()
                .map(ProviderCode::of)
                .toList(); // <-- Читаем дешёвую проекцию, без загрузки сущностей
    }
}
