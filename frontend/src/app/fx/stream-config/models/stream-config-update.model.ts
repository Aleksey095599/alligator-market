/** DTO обновления конфигурации стрима аналогичный backend */
export interface StreamConfigUpdateDto {
  priority: number;
  refreshMs: number;
  enabled: boolean;
}
