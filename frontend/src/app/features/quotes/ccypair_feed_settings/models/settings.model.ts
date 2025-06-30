/** DTO настроек потока котировок аналогичный backend */
export interface SettingsDto {
  pair: string;
  provider: string;
  priority: number;
  fetchPeriodMs: number; // минимум 1000 или 0 для PUSH
  enabled: boolean;
}
