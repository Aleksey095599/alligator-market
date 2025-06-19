/** DTO конфигурации стрима аналогичный backend */
export interface StreamConfigDto {
  pair: string;
  provider: string;
  priority: number;
  refreshMs: number;
  enabled: boolean;
}
