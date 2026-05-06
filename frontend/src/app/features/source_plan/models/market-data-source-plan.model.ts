export type MarketDataSourceLifecycleStatus = 'ACTIVE' | 'RETIRED';

export interface MarketDataSourceResponseDto {
  providerCode: string;
  priority: number;
  lifecycleStatus: MarketDataSourceLifecycleStatus;
}

export interface MarketDataSourcePlanResponseDto {
  captureProcessCode: string;
  instrumentCode: string;
  sources: MarketDataSourceResponseDto[];
}

export interface MarketDataSourcePlanListResponseDto {
  plans: MarketDataSourcePlanResponseDto[];
}
