package com.alligator.market.backend.provider.catalog.passport.persistence.jpa;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import com.alligator.market.domain.provider.model.vo.ProviderCode;

import java.util.List;

/**
 * Spring Data JPA-репозиторий для паспортов провайдеров рыночных данных.
 */
public interface PassportJpaRepository extends JpaRepository<PassportEntity, Long> {

    /**
     * Возвращает список всех кодов провайдеров.
     *
     * <p>Запрос оптимизирован как "дешёвая проекция" с помощью {@link Query} и {@link QueryHints}.</p>
     */
    @Query("select p.providerCode from PassportEntity p")
    @QueryHints(@QueryHint(name = org.hibernate.jpa.HibernateHints.HINT_READ_ONLY, value = "true"))
    List<ProviderCode> findAllCodes();
}
