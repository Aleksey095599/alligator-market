import { MarketDataSourceRequestDto } from './create-source-plan.model';

export interface ReplaceSourcePlanDto {
  sources: MarketDataSourceRequestDto[];
}
