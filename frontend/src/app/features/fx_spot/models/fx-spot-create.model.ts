import { FxSpotValueDate } from './fx-spot-value-date.model';

/** DTO для создания инструмента FX_SPOT аналогичный backend. */
export interface FxSpotCreateDto {
  /* Код базовой валюты */
  baseCurrency: string;
  /* Код котируемой валюты */
  quoteCurrency: string;
  /* Количество знаков после запятой */
  defaultQuoteFractionDigits: number;
  /* Дата валютирования */
  valueDate: FxSpotValueDate;
}
