export interface MarketDataSourceResponseDto {
  providerCode: string;
  active: boolean;
  priority: number;
}

export interface InstrumentSourcePlanResponseDto {
  instrumentCode: string;
  sources: MarketDataSourceResponseDto[];
}

export interface InstrumentSourcePlanListResponseDto {
  plans: InstrumentSourcePlanResponseDto[];
}
