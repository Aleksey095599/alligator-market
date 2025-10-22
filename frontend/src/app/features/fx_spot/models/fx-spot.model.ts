import { FxSpotValueDate } from './fx-spot-value-date.model';

/** Основной DTO инструмента FX_SPOT аналогичный backend. */
export interface FxSpotDto {
  /* Код базовой валюты */
  baseCurrency: string;
  /* Код котируемой валюты */
  quoteCurrency: string;
  /* Количество знаков после запятой */
  defaultQuoteFractionDigits: number;
  /* Дата валютирования */
  valueDate: FxSpotValueDate;
}

/** DTO строки списка с символом инструмента. */
export interface FxSpotListItemDto {
  /* Символ инструмента (например, EURUSD_TOD) */
  symbol: string;
  /* Код базовой валюты */
  baseCurrency: string;
  /* Код котируемой валюты */
  quoteCurrency: string;
  /* Количество знаков после запятой */
  defaultQuoteFractionDigits: number;
  /* Дата валютирования */
  valueDate: FxSpotValueDate;
}
