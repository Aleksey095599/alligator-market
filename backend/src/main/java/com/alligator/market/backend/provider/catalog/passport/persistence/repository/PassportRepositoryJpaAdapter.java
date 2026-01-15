package com.alligator.market.backend.provider.catalog.passport.persistence.repository;

import com.alligator.market.backend.provider.catalog.passport.persistence.jpa.PassportJpaRepository;
import com.alligator.market.domain.provider.code.ProviderCode;
import com.alligator.market.domain.provider.repository.passport.ProviderPassportRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Адаптер доменного репозитория паспортов провайдеров (в контексте Spring Data JPA).
 *
 * <p>{@link Transactional} в режиме только чтение, так как таблица {@code provider_passport} — статичный справочник
 * метаданных провайдеров.</p>
 */
@Repository
@Transactional(readOnly = true)
public class PassportRepositoryJpaAdapter implements ProviderPassportRepository {

    /* JPA-репозиторий. */
    private final PassportJpaRepository jpa;

    /* Конструктор. */
    public PassportRepositoryJpaAdapter(PassportJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<ProviderCode> findAllCodes() {
        return jpa.findAllCodes(); // <-- Читаем дешёвую проекцию, без загрузки сущностей
    }
}
