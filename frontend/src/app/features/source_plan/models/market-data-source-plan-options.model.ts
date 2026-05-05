export interface MarketDataCaptureProcessOptionDto {
  code: string;
  displayName: string;
}

export interface InstrumentOptionDto {
  code: string;
}

export interface ProviderOptionDto {
  code: string;
}

export interface MarketDataSourcePlanOptionsResponseDto {
  captureProcesses: MarketDataCaptureProcessOptionDto[];
  instruments: InstrumentOptionDto[];
  providers: ProviderOptionDto[];
}
