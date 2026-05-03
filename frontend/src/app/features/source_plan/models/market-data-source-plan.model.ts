export interface MarketDataSourceResponseDto {
  providerCode: string;
  active: boolean;
  priority: number;
}

export interface MarketDataSourcePlanResponseDto {
  collectionProcessCode: string;
  instrumentCode: string;
  sources: MarketDataSourceResponseDto[];
}

export interface MarketDataSourcePlanListResponseDto {
  plans: MarketDataSourcePlanResponseDto[];
}
