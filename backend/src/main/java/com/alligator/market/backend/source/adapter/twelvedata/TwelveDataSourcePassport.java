package com.alligator.market.backend.source.adapter.twelvedata;

import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.vo.SourceCode;

public final class TwelveDataSourcePassport {
    public static final String SOURCE_CODE_VALUE = "TWELVE_DATA";
    public static final SourceCode SOURCE_CODE = SourceCode.of(SOURCE_CODE_VALUE);
    public static final SourcePassport INSTANCE = new SourcePassport(
            SourceDisplayName.of("Twelve Data")
    );

    private TwelveDataSourcePassport() {
        throw new UnsupportedOperationException("Utility class");
    }
}
