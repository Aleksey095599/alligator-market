import { ValueDateCode } from './value-date-code.model';

/** Основной DTO инструмента FX_SPOT аналогичный backend. */
export interface FxSpotDto {
  /* Код базовой валюты */
  baseCurrency: string;
  /* Код котируемой валюты */
  quoteCurrency: string;
  /* Количество знаков после запятой */
  quoteDecimal: number;
  /* Код даты валютирования */
  valueDateCode: ValueDateCode;
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
  quoteDecimal: number;
  /* Код даты валютирования */
  valueDateCode: ValueDateCode;
}
