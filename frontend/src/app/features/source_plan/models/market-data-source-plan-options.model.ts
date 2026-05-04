export interface CaptureProcessOptionDto {
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
  captureProcesses: CaptureProcessOptionDto[];
  instruments: InstrumentOptionDto[];
  providers: ProviderOptionDto[];
}
