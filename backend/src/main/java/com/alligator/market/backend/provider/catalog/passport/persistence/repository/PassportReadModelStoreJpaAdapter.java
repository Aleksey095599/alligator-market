package com.alligator.market.backend.provider.catalog.passport.persistence.repository;

import com.alligator.market.backend.provider.catalog.passport.persistence.jpa.PassportJpaRepository;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.readmodel.store.passport.ProviderPassportReadModelStore;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Адаптер доменного {@link ProviderPassportReadModelStore} (контекст Spring Data JPA).
 *
 * <p>Примечание: {@link Transactional} с {@code readOnly} – потому что таблица БД с пасспортами провайдеров это
 * статичный справочник метаданных.</p>
 */
@Repository
@Transactional(readOnly = true)
public class PassportReadModelStoreJpaAdapter implements ProviderPassportReadModelStore {

    /* JPA-репозиторий. */
    private final PassportJpaRepository jpa;

    /* Конструктор. */
    public PassportReadModelStoreJpaAdapter(PassportJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<ProviderCode> findAllCodes() {
        return jpa.findAllCodes(); // <-- Читаем дешёвую проекцию, без загрузки сущностей
    }
}
