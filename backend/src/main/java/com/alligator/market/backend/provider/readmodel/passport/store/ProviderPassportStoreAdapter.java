package com.alligator.market.backend.provider.readmodel.passport.store;

import com.alligator.market.domain.provider.model.passport.ProviderPassport;
import com.alligator.market.domain.provider.model.vo.ProviderCode;
import com.alligator.market.domain.provider.readmodel.passport.store.ProviderPassportStore;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Адаптер доменного хранилища {@link ProviderPassportStore}.
 */
public class ProviderPassportStoreAdapter implements ProviderPassportStore.Read, ProviderPassportStore.Write {

    private final ProviderPassportStore.Read read;
    private final ProviderPassportStore.Write write;

    public ProviderPassportStoreAdapter(ProviderPassportStore.Read read, ProviderPassportStore.Write write) {
        this.read = Objects.requireNonNull(read, "read must not be null");
        this.write = Objects.requireNonNull(write, "write must not be null");
    }

    @Override
    public List<ProviderCode> findAllCodes() {
        return read.findAllCodes();
    }

    @Override
    public void deleteByCodes(Collection<ProviderCode> codes) {
        write.deleteByCodes(codes);
    }

    @Override
    public void upsertAll(Map<ProviderCode, ProviderPassport> passports) {
        write.upsertAll(passports);
    }
}
