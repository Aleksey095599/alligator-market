package com.alligator.market.domain.provider.context_sync;

import com.alligator.market.domain.provider.profile.ProviderProfile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Результат сопоставления профилей провайдеров.
 */
public record ContextDiff(
        List<ProviderProfile> add,
        Map<ProviderProfile, Long> replaced,
        Map<ProviderProfile, Long> missing
) {

    public ContextDiff() {
        this(new ArrayList<>(), new LinkedHashMap<>(), new LinkedHashMap<>());
    }

    public void putToAddList(ProviderProfile profile) {
        add.add(profile);
    }

    public void putToReplaceMap(ProviderProfile profile, Long id) {
        replaced.put(profile, id);
    }

    public void putToMissingMap(ProviderProfile profile, Long id) {
        missing.put(profile, id);
    }
}
