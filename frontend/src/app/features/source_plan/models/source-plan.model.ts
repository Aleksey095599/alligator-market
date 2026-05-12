export type SourceLifecycleStatus = 'ACTIVE' | 'RETIRED';
export type MarketDataCapturerLifecycleStatus = 'ACTIVE' | 'RETIRED';
export type SourcePlanExecutionStatus =
  | 'EXECUTABLE'
  | 'CAPTURER_RETIRED'
  | 'NO_EXECUTABLE_SOURCES';

export interface SourceResponseDto {
  sourceCode: string;
  priority: number;
  lifecycleStatus: SourceLifecycleStatus;
}

export interface SourcePlanResponseDto {
  capturerCode: string;
  capturerLifecycleStatus: MarketDataCapturerLifecycleStatus;
  planExecutionStatus: SourcePlanExecutionStatus;
  instrumentCode: string;
  sources: SourceResponseDto[];
}

export interface SourcePlanListResponseDto {
  plans: SourcePlanResponseDto[];
}
