package com.alligator.market.domain.instrument.asset.forex.type;

import com.alligator.market.domain.instrument.asset.AssetClass;

/**
 * Тип контракта финансового инструмента класса {@link AssetClass#FOREX}.
 */
public enum ForexContractType {
    SPOT,
    FORWARD,
    SWAP
}
