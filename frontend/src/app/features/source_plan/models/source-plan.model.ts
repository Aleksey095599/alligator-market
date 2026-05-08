export type MarketDataSourceLifecycleStatus = 'ACTIVE' | 'RETIRED';

export interface MarketDataSourceResponseDto {
  sourceCode: string;
  priority: number;
  lifecycleStatus: MarketDataSourceLifecycleStatus;
}

export interface SourcePlanResponseDto {
  capturerCode: string;
  instrumentCode: string;
  sources: MarketDataSourceResponseDto[];
}

export interface SourcePlanListResponseDto {
  plans: SourcePlanResponseDto[];
}
