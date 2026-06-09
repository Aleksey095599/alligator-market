package com.alligator.market.domain.capturer.passport;

import com.alligator.market.domain.capturer.passport.vo.CapturerDisplayName;
import com.alligator.market.domain.shared.vo.StringValueNormalizer;

import java.util.Objects;

public record CapturerPassport(
        CapturerDisplayName displayName,
        String description
) {
    private static final int MAX_DESCRIPTION_LENGTH = 100;
    private static final StringValueNormalizer.Options DESCRIPTION_NORMALIZATION_OPTIONS =
            StringValueNormalizer.options()
                    .maxLength(MAX_DESCRIPTION_LENGTH)
                    .rejectControlCharacters()
                    .build();

    public CapturerPassport {
        Objects.requireNonNull(displayName, "displayName must not be null");
        description = StringValueNormalizer.normalize(
                description,
                "description",
                DESCRIPTION_NORMALIZATION_OPTIONS
        );
    }
}
