import { MarketDataSourceRequestDto } from './create-market-data-source-plan.model';

export interface ReplaceMarketDataSourcePlanDto {
  sources: MarketDataSourceRequestDto[];
}
