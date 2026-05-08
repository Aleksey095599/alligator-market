export interface MarketDataCapturerOptionDto {
  code: string;
  displayName: string;
}

export interface InstrumentOptionDto {
  code: string;
}

export interface MarketDataSourceOptionDto {
  code: string;
}

export interface MarketDataSourcePlanOptionsResponseDto {
  capturers: MarketDataCapturerOptionDto[];
  instruments: InstrumentOptionDto[];
  sources: MarketDataSourceOptionDto[];
}
