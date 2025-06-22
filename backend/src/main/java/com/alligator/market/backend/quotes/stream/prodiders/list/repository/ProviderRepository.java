package com.alligator.market.backend.quotes.stream.prodiders.list.repository;

import com.alligator.market.backend.quotes.stream.prodiders.list.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Репозиторий для работы с таблицей provider.
 */
public interface ProviderRepository extends JpaRepository<Provider, Long> {

    Optional<Provider> findByName(String name);

    Optional<Provider> findByNameAndMode(String name, String mode);

}
