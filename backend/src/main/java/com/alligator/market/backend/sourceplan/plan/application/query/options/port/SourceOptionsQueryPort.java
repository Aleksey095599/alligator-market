package com.alligator.market.backend.sourceplan.plan.application.query.options.port;

import com.alligator.market.domain.source.vo.SourceCode;

import java.util.List;

public interface SourceOptionsQueryPort {
    List<SourceCode> findAllSourceCodes();
}
