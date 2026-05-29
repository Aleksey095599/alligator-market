export type SourceLifecycleStatus = 'AVAILABLE' | 'SOURCE_RETIRED';
export type CapturerLifecycleStatus = 'REGISTERED' | 'RETIRED';
export type SourcePlanExecutionStatus =
  | 'AVAILABLE'
  | 'CAPTURER_RETIRED'
  | 'NO_AVAILABLE_SOURCES';

export interface SourceResponseDto {
  sourceCode: string;
  priority: number;
  lifecycleStatus: SourceLifecycleStatus;
}

export interface SourcePlanResponseDto {
  capturerCode: string;
  capturerLifecycleStatus: CapturerLifecycleStatus;
  planExecutionStatus: SourcePlanExecutionStatus;
  instrumentCode: string;
  sources: SourceResponseDto[];
}

export interface SourcePlanListResponseDto {
  plans: SourcePlanResponseDto[];
}
