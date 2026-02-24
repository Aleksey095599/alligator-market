package com.alligator.market.backend.provider.readmodel.passport.store.read.jpa;

import com.alligator.market.backend.provider.catalog.passport.persistence.jpa.PassportJpaRepository;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.readmodel.passport.store.ProviderPassportProjectionStore;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * JPA-адаптер write-порта {@link ProviderPassportProjectionStore} (контекст Spring Data JPA).
 */
@Repository
@Transactional(readOnly = true)
public class ProviderPassportReadStoreJpaAdapter implements ProviderPassportProjectionStore.Read {

    private final PassportJpaRepository jpa;

    public ProviderPassportReadStoreJpaAdapter(PassportJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public List<ProviderCode> findAllCodes() {
        return jpa.findAllCodes();
    }
}
