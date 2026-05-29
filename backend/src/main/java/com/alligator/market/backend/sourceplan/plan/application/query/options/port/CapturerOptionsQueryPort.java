package com.alligator.market.backend.sourceplan.plan.application.query.options.port;

import com.alligator.market.backend.sourceplan.plan.application.query.options.model.CapturerOption;

import java.util.List;

public interface CapturerOptionsQueryPort {
    List<CapturerOption> findAllCapturers();
}
