package com.alligator.market.domain.provider.context_sync;

import com.alligator.market.domain.provider.profile.model.ProviderProfile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Модель результата сопоставления профилей провайдеров рыночных данных в виде трех соответствующих списков,
 * касательно которых требуются действия для целей синхронизации.
 */
public record ContextDiff(
        List<ProviderProfile> add,
        List<Long> replaced,
        List<Long> missing
) {

    public ContextDiff() {
        this(new ArrayList<>(), new LinkedList<>(), new LinkedList<>());
    }

    public void putToAddList(ProviderProfile profile) {
        add.add(profile);
    }

    public void putToReplaceList(Long id) {
        replaced.add(id);
    }

    public void putToMissingList(Long id) {
        missing.add(id);
    }
}
