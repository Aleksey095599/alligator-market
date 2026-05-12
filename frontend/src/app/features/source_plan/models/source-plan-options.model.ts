export interface MarketDataCapturerOptionDto {
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
  capturers: MarketDataCapturerOptionDto[];
  instruments: InstrumentOptionDto[];
  sources: SourceOptionDto[];
}
