/** DTO инструмента FX OUTRIGHT аналогичный backend. */
export interface FxOutrightDto {
  /* Внутренний код инструмента */
  internalCode: string;
  /* Код валютной пары */
  pairCode: string;
  /* Код даты расчётов */
  valueDateCode: string;
}
