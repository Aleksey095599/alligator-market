export interface MarketDataSourceRequestDto {
  providerCode: string;
  active: boolean;
  priority: number;
}

export interface CreateMarketDataSourcePlanDto {
  instrumentCode: string;
  sources: MarketDataSourceRequestDto[];
}
