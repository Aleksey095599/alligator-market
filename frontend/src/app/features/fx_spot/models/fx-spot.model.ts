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

/** DTO строки списка с фактическим кодом и символом. */
export interface FxSpotListItemDto {
  /* Код инструмента от backend */
  code: string;
  /* Символ инструмента (например, EUR/USD TOD) */
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
