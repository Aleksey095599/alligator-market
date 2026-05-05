export interface MarketDataSourceResponseDto {
  providerCode: string;
  priority: number;
}

export interface MarketDataSourcePlanResponseDto {
  captureProcessCode: string;
  instrumentCode: string;
  sources: MarketDataSourceResponseDto[];
}

export interface MarketDataSourcePlanListResponseDto {
  plans: MarketDataSourcePlanResponseDto[];
}
