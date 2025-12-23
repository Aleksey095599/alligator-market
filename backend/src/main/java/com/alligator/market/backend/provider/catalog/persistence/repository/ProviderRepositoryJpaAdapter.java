package com.alligator.market.backend.provider.catalog.persistence.repository;

import com.alligator.market.backend.provider.catalog.persistence.jpa.ProviderJpaRepository;
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
    public List<String> findAllCodes() {
        return jpa.findAllCodes(); // <-- Читаем дешёвую проекцию, без загрузки сущностей
    }
}
