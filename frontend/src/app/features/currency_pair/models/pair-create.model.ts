/** DTO для создания валютной пары аналогичный backend */
export interface PairCreateDto {
  code1: string;
  code2: string;
  decimal: number;
}
