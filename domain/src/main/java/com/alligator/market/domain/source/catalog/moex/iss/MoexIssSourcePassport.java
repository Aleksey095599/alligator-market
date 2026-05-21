package com.alligator.market.domain.source.catalog.moex.iss;

import com.alligator.market.domain.source.passport.SourcePassport;
import com.alligator.market.domain.source.passport.vo.SourceDisplayName;
import com.alligator.market.domain.source.vo.SourceCode;

public final class MoexIssSourcePassport {
    public static final String SOURCE_CODE_VALUE = "MOEX_ISS";
    public static final SourceCode SOURCE_CODE = SourceCode.of(SOURCE_CODE_VALUE);
    public static final SourcePassport INSTANCE = new SourcePassport(
            SourceDisplayName.of("MOEX Informational & Statistical Server")
    );

    private MoexIssSourcePassport() {
        throw new UnsupportedOperationException("Utility class");
    }
}
