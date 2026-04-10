export interface MarketDataSourceRequestDto {
  providerCode: string;
  active: boolean;
  priority: number;
}

export interface CreateInstrumentSourcePlanDto {
  instrumentCode: string;
  sources: MarketDataSourceRequestDto[];
}
