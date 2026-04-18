export interface InstrumentOptionDto {
  code: string;
}

export interface ProviderOptionDto {
  code: string;
}

export interface InstrumentSourcePlanOptionsResponseDto {
  instruments: InstrumentOptionDto[];
  providers: ProviderOptionDto[];
}
