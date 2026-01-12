package com.alligator.market.backend.provider.catalog.passport.persistence.jpa;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

/**
 * Spring Data JPA-репозиторий для работы с провайдерами рыночных данных.
 */
public interface ProviderJpaRepository extends JpaRepository<ProviderPassportEntity, Long> {

    /**
     * Найти все коды провайдеров (натуральные ключи).
     */
    @Query("select p.providerCode from ProviderPassportEntity p")
    @QueryHints(@QueryHint(name = org.hibernate.jpa.HibernateHints.HINT_READ_ONLY, value = "true"))
    List<String> findAllCodes();
}
