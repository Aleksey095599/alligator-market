/** DTO для обновления валюты аналогичный backend */
export interface CurrencyUpdateDto {
  name: string;
  country: string;
  decimal: number;
}
