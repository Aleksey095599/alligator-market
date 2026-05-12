export interface SourceRequestDto {
  sourceCode: string;
  priority: number;
}

export interface CreateSourcePlanDto {
  capturerCode: string;
  instrumentCode: string;
  sources: SourceRequestDto[];
}
