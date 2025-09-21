/** DTO для обновления валюты аналогичный backend. */
export interface UpdateCurrencyDto {
  name: string;
  country: string;
  decimal: number;
}
