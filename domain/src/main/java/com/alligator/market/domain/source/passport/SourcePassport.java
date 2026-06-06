package com.alligator.market.domain.source.passport;

import com.alligator.market.domain.shared.vo.StringValueNormalizer;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;

import java.util.Objects;

public record SourcePassport(
        SourceDisplayName displayName,
        String description
) {
    private static final int MAX_DESCRIPTION_LENGTH = 100;
    private static final StringValueNormalizer.Options DESCRIPTION_NORMALIZATION_OPTIONS =
            StringValueNormalizer.options()
                    .maxLength(MAX_DESCRIPTION_LENGTH)
                    .rejectControlCharacters()
                    .build();

    public SourcePassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
        description = StringValueNormalizer.normalize(
                description,
                "description",
                DESCRIPTION_NORMALIZATION_OPTIONS
        );
    }
}
