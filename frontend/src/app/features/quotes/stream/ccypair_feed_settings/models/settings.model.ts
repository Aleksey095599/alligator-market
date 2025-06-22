/** DTO настроек потока котировок аналогичный backend */
export interface SettingsDto {
  pair: string;
  provider: string;
  mode: string;
  priority: number;
  refreshMs: number;
  enabled: boolean;
}
