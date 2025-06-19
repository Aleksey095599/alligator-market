/** DTO создания конфигурации стрима аналогичный backend */
export interface StreamConfigCreateDto {
  pair: string;
  provider: string;
  priority: number;
  refreshMs: number;
  enabled: boolean;
}
