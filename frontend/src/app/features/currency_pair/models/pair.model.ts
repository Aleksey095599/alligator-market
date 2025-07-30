/** DTO для валютной пары аналогичный backend. */
export interface PairDto {
  /* Код базовой валюты */
  base: string;
  /* Код котируемой валюты */
  quote: string;
  /* Символ валютной пары (base + quote) */
  symbol: string;
  /* Кол-во знаков после запятой */
  decimal: number;
}
