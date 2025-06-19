/** DTO создания настроек потока котировок аналогичный backend */
export interface SettingsCreateDto {
  pair: string;
  provider: string;
  /** Режим получения котировок */
  mode: string;
  priority: number;
  refreshMs: number;
  enabled: boolean;
}
