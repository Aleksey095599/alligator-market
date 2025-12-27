/** Общий DTO валюты (общий контракт). */
export interface CurrencyDto {
  code: string;
  name: string;
  country: string;
  fractionDigits: number;
}

/** DTO создания валюты (in). */
export type CreateCurrencyDto = CurrencyDto;

/** DTO ответа по валюте (out). */
export type CurrencyResponseDto = CurrencyDto;
