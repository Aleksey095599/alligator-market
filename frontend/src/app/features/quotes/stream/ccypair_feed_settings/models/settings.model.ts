/** DTO настроек потока котировок аналогичный backend */
export interface SettingsDto {
  pair: string;
  provider: string;
  priority: number;
  fetchPeriodMs: number;
  enabled: boolean;
}
