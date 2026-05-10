export type MarketDataSourceLifecycleStatus = 'ACTIVE' | 'RETIRED';
export type MarketDataCapturerLifecycleStatus = 'ACTIVE' | 'RETIRED';
export type SourcePlanExecutionStatus =
  | 'EXECUTABLE'
  | 'CAPTURER_RETIRED'
  | 'NO_EXECUTABLE_SOURCES';

export interface MarketDataSourceResponseDto {
  sourceCode: string;
  priority: number;
  lifecycleStatus: MarketDataSourceLifecycleStatus;
}

export interface SourcePlanResponseDto {
  capturerCode: string;
  capturerLifecycleStatus: MarketDataCapturerLifecycleStatus;
  planExecutionStatus: SourcePlanExecutionStatus;
  instrumentCode: string;
  sources: MarketDataSourceResponseDto[];
}

export interface SourcePlanListResponseDto {
  plans: SourcePlanResponseDto[];
}
