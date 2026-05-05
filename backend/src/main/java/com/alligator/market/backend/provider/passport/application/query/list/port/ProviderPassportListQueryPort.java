package com.alligator.market.backend.provider.passport.application.query.list.port;

import com.alligator.market.backend.provider.passport.application.query.list.model.ProviderPassportListItem;

import java.util.List;

/**
 * Port for reading provider passport projection rows.
 */
public interface ProviderPassportListQueryPort {

    /**
     * Returns all provider passport projection rows, including retired rows.
     */
    List<ProviderPassportListItem> findAll();
}
