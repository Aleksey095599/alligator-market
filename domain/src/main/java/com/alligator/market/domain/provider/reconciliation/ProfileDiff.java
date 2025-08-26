package com.alligator.market.domain.provider.reconciliation;

import com.alligator.market.domain.provider.profile.model.Profile;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель результата сопоставления профилей провайдеров в виде трех списков,
 * касательно которых требуются действия с репозиторием для целей синхронизации с контекстом.
 */
public record ProfileDiff(
        List<Profile> add,
        List<Long> replaced,
        List<Long> missing
) {
    public ProfileDiff() {
        this(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public void putToAddList(Profile profile) {
        add.add(profile);
    }
    public void putToReplaceList(Long id) {
        replaced.add(id);
    }
    public void putToMissingList(Long id) {
        missing.add(id);
    }
}
