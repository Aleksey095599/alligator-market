/** DTO котировки аналогичный backend */
export interface CurrencyQuoteDto {
  pairId: number;
  bid: string;
  ask: string;
  ts: string;
}
