package com.alligator.market.domain.provider.context_sync;

import com.alligator.market.domain.provider.profile.ProviderProfile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Результат сопоставления профилей провайдеров.
 */
public class ContextDiff {

    /** Список профилей, которые нужно создать. */
    private final List<ProviderProfile> add = new ArrayList<>();

    /** Профили для пометки статусом REPLACED. */
    private final Map<ProviderProfile, Long> replaced = new LinkedHashMap<>();

    /** Профили для пометки статусом MISSING. */
    private final Map<ProviderProfile, Long> missing = new LinkedHashMap<>();

    public List<ProviderProfile> getAdd() {
        return add;
    }

    public Map<ProviderProfile, Long> getReplaced() {
        return replaced;
    }

    public Map<ProviderProfile, Long> getMissing() {
        return missing;
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
