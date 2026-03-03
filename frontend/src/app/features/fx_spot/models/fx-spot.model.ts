import { FxSpotTenor } from './fx-spot-tenor.model';

/** Основной DTO инструмента FX_SPOT аналогичный backend. */
export interface FxSpotDto {
  /* Класс актива */
  assetClass: string;
  /* Тип контракта */
  contractType: string;
  /* Код базовой валюты */
  baseCurrency: string;
  /* Код котируемой валюты */
  quoteCurrency: string;
  /* Количество знаков после запятой */
  defaultQuoteFractionDigits: number;
  /* Тенор даты валютирования */
  tenor: FxSpotTenor;
}

/** DTO строки списка с символом инструмента. */
export interface FxSpotListItemDto {
  /* Уникальный код инструмента (например, FX_SPOT_EURUSD_TOD) */
  instrumentCode: string;
  /* Символ инструмента (например, EURUSD_TOD) */
  symbol: string;
  /* Класс актива */
  assetClass: string;
  /* Тип контракта */
  contractType: string;
  /* Код базовой валюты */
  baseCurrency: string;
  /* Код котируемой валюты */
  quoteCurrency: string;
  /* Количество знаков после запятой */
  defaultQuoteFractionDigits: number;
  /* Тенор даты валютирования */
  tenor: FxSpotTenor;
}
