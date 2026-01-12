package com.alligator.market.backend.provider.catalog.passport.persistence.jpa;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.alligator.market.domain.provider.code.ProviderCode;

import java.util.List;

/**
 * Spring Data JPA-репозиторий для паспортов провайдеров рыночных данных.
 */
public interface ProviderPassportJpaRepository extends JpaRepository<ProviderPassportEntity, Long> {

    /**
     * Найти все коды провайдеров (натуральные ключи).
     */
    @Query("select p.providerCode from ProviderPassportEntity p")
    @QueryHints(@QueryHint(name = org.hibernate.jpa.HibernateHints.HINT_READ_ONLY, value = "true"))
    List<ProviderCode> findAllCodes();
}
