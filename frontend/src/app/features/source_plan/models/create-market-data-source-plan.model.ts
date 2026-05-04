export interface MarketDataSourceRequestDto {
  providerCode: string;
  active: boolean;
  priority: number;
}

export interface CreateMarketDataSourcePlanDto {
  captureProcessCode: string;
  instrumentCode: string;
  sources: MarketDataSourceRequestDto[];
}
