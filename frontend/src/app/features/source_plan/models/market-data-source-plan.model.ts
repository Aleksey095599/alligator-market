export type MarketDataSourceLifecycleStatus = 'ACTIVE' | 'RETIRED';

export interface MarketDataSourceResponseDto {
  sourceCode: string;
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
