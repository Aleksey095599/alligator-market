package com.alligator.market.domain.instrument.model;

import com.alligator.market.domain.instrument.asset.AssetClass;
import com.alligator.market.domain.instrument.asset.forex.type.ForexContractType;

/**
 * Тип финансового инструмента: сочетание класса активов и типа контракта.
 */
public enum InstrumentTypeNew {

    FX_SPOT(AssetClass.FOREX, ForexContractType.SPOT),
    FX_FORWARD(AssetClass.FOREX, ForexContractType.FORWARD),
    FX_SWAP(AssetClass.FOREX, ForexContractType.SWAP);

    private final AssetClass assetClass;
    private final ForexContractType contractType;

    InstrumentTypeNew(AssetClass assetClass, ForexContractType contractType) {
        this.assetClass = assetClass;
        this.contractType = contractType;
    }

    public AssetClass assetClass() {
        return assetClass;
    }

    public ForexContractType contractType() {
        return contractType;
    }
}
