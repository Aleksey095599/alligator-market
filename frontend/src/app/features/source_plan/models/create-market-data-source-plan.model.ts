export interface MarketDataSourceRequestDto {
  providerCode: string;
  priority: number;
}

export interface CreateMarketDataSourcePlanDto {
  captureProcessCode: string;
  instrumentCode: string;
  sources: MarketDataSourceRequestDto[];
}
