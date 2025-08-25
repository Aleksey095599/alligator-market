/** DTO для создания валютной пары аналогичный backend. */
export interface PairCreateDto {
  /* Код базовой валюты */
  base: string;
  /* Код котируемой валюты */
  quote: string;
  /* Количество знаков после запятой */
  decimal: number;
}
