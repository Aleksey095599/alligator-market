export interface MarketDataCaptureProcessOptionDto {
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
  captureProcesses: MarketDataCaptureProcessOptionDto[];
  instruments: InstrumentOptionDto[];
  sources: MarketDataSourceOptionDto[];
}
