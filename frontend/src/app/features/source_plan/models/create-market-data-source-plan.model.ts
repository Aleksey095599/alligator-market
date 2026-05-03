export interface MarketDataSourceRequestDto {
  providerCode: string;
  active: boolean;
  priority: number;
}

export interface CreateMarketDataSourcePlanDto {
  collectionProcessCode: string;
  instrumentCode: string;
  sources: MarketDataSourceRequestDto[];
}
