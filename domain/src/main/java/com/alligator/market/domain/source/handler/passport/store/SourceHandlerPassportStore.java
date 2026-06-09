package com.alligator.market.domain.source.handler.passport.store;

import java.util.Collection;
import java.util.Set;

public interface SourceHandlerPassportStore {

    void retireAllExcept(Set<SourceHandlerPassportKey> registeredSourceHandlerPassportKeys);

    void save(Collection<SourceHandlerPassportRecord> passports);
}
