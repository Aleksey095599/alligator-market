/** DTO создания настроек потока котировок аналогичный backend */
export interface SettingsCreateDto {
  pair: string;
  provider: string;
  mode: string;
  priority: number;
  refreshMs: number;
  enabled: boolean;
}
