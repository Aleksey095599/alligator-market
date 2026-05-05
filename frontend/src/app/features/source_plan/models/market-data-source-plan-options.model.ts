export interface MDCaptureProcessOptionDto {
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
  captureProcesses: MDCaptureProcessOptionDto[];
  instruments: InstrumentOptionDto[];
  providers: ProviderOptionDto[];
}
