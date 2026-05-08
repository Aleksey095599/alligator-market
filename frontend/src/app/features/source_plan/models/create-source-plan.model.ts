export interface MarketDataSourceRequestDto {
  sourceCode: string;
  priority: number;
}

export interface CreateSourcePlanDto {
  capturerCode: string;
  instrumentCode: string;
  sources: MarketDataSourceRequestDto[];
}
