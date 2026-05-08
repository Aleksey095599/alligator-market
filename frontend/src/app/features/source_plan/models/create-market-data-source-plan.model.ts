export interface MarketDataSourceRequestDto {
  sourceCode: string;
  priority: number;
}

export interface CreateMarketDataSourcePlanDto {
  capturerCode: string;
  instrumentCode: string;
  sources: MarketDataSourceRequestDto[];
}
