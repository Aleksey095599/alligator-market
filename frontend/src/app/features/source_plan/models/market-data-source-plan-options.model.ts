export interface InstrumentOptionDto {
  code: string;
}

export interface ProviderOptionDto {
  code: string;
}

export interface MarketDataSourcePlanOptionsResponseDto {
  instruments: InstrumentOptionDto[];
  providers: ProviderOptionDto[];
}
