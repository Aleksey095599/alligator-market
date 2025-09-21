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

/** DTO для отображения инструмента с вычисленным кодом. */
export interface FxSpotItemDto extends FxSpotDto {
  /* Код инструмента (base + quote + '_' + valueDateCode) */
  instrumentCode: string;
}
