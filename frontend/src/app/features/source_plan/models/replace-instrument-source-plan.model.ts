import { MarketDataSourceRequestDto } from './create-instrument-source-plan.model';

export interface ReplaceInstrumentSourcePlanDto {
  sources: MarketDataSourceRequestDto[];
}
