/** DTO настроек потока котировок аналогичный backend */
export interface SettingsDto {
  pair: string;
  provider: string;
  /** Режим получения котировок */
  mode: string;
  priority: number;
  refreshMs: number;
  enabled: boolean;
}
