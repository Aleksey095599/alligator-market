/** DTO для валюты аналогичный backend. */
export interface CurrencyDto {
  code: string;
  name: string;
  country: string;
  fractionDigits: number;
}
