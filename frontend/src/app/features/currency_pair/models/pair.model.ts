/** DTO для валютной пары аналогичный backend. */
export interface PairDto {
  /* Код базовой валюты */
  base: string;
  /* Код котируемой валюты */
  quote: string;
  /* Код валютной пары (base + quote) */
  pairCode: string;
  /* Кол-во знаков после запятой */
  decimal: number;
}
