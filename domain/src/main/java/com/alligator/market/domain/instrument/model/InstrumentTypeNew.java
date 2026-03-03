package com.alligator.market.domain.instrument.model;

/**
 * Тип финансового инструмента: сочетание класса активов и типа контракта.
 */
public enum InstrumentTypeNew {

    FX_SPOT(AssetClass.FOREX, ContractType.SPOT),
    FX_FWD(AssetClass.FOREX, ContractType.FORWARD),
    FX_SWAP(AssetClass.FOREX, ContractType.SWAP);

    private final AssetClass assetClass;
    private final ContractType contractType;

    InstrumentTypeNew(AssetClass assetClass, ContractType contractType) {
        this.assetClass = assetClass;
        this.contractType = contractType;
    }

    public AssetClass assetClass() {
        return assetClass;
    }

    public ContractType contractType() {
        return contractType;
    }
}
