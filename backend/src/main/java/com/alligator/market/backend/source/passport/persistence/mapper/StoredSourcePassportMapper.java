package com.alligator.market.backend.source.passport.persistence.mapper;

import com.alligator.market.backend.source.passport.persistence.model.StoredSourcePassport;
import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.vo.SourceCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alligator.market.domain.source.passport.registry.stored.StoredSourcePassportRegistryStatus.REGISTERED;

public final class StoredSourcePassportMapper {

    public List<StoredSourcePassport> toRegisteredStored(
            Map<SourceCode, SourcePassport> passports
    ) {
        if (passports == null) {
            throw new IllegalArgumentException("passports must not be null");
        }

        List<StoredSourcePassport> storedPassports = new ArrayList<>(passports.size());
        for (Map.Entry<SourceCode, SourcePassport> entry : passports.entrySet()) {
            SourceCode sourceCode = entry.getKey();
            if (sourceCode == null) {
                throw new IllegalArgumentException("passports must not contain null keys");
            }

            SourcePassport passport = entry.getValue();
            if (passport == null) {
                throw new IllegalArgumentException("passports must not contain null values");
            }

            storedPassports.add(toRegisteredStored(sourceCode, passport));
        }

        return storedPassports;
    }

    public StoredSourcePassport toRegisteredStored(
            SourceCode sourceCode,
            SourcePassport passport
    ) {
        return new StoredSourcePassport(sourceCode, passport, REGISTERED);
    }
}
