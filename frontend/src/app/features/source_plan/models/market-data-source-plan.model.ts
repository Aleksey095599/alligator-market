export interface MarketDataSourceResponseDto {
  providerCode: string;
  active: boolean;
  priority: number;
}

export interface MarketDataSourcePlanResponseDto {
  instrumentCode: string;
  sources: MarketDataSourceResponseDto[];
}

export interface MarketDataSourcePlanListResponseDto {
  plans: MarketDataSourcePlanResponseDto[];
}
