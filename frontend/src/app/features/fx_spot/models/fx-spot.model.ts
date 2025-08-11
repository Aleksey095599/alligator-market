/** DTO инструмента FX SPOT аналогичный backend. */
export interface FxSpotDto {
  /* Внутренний код инструмента */
  internalCode: string;
  /* Код валютной пары */
  pairCode: string;
  /* Код даты расчётов */
  valueDateCode: string;
}
