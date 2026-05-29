export interface CapturerOptionDto {
  code: string;
  displayName: string;
}

export interface InstrumentOptionDto {
  code: string;
}

export interface SourceOptionDto {
  code: string;
}

export interface SourcePlanOptionsResponseDto {
  capturers: CapturerOptionDto[];
  instruments: InstrumentOptionDto[];
  sources: SourceOptionDto[];
}
