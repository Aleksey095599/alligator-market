export type SourceLifecycleStatus = 'AVAILABLE' | 'SOURCE_RETIRED';
export type CapturerRegistryStatus = 'REGISTERED' | 'RETIRED';
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
  capturerRegistryStatus: CapturerRegistryStatus;
  planExecutionStatus: SourcePlanExecutionStatus;
  instrumentCode: string;
  sources: SourceResponseDto[];
}

export interface SourcePlanListResponseDto {
  plans: SourcePlanResponseDto[];
}
