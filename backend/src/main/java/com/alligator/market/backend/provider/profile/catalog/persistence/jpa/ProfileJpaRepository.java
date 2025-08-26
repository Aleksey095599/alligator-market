package com.alligator.market.backend.provider.profile.catalog.persistence.jpa;

import com.alligator.market.domain.provider.profile.model.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA-репозиторий для работы с профилями провайдеров.
 */
public interface ProfileJpaRepository extends JpaRepository<ProfileEntity, Long> {

    /** Найти все профили по заданному статусу. */
    List<ProfileEntity> findAllByProfileStatus(ProfileStatus profileStatus);
}

