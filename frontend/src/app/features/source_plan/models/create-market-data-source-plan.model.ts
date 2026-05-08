export interface MarketDataSourceRequestDto {
  sourceCode: string;
  priority: number;
}

export interface CreateMarketDataSourcePlanDto {
  captureProcessCode: string;
  instrumentCode: string;
  sources: MarketDataSourceRequestDto[];
}
