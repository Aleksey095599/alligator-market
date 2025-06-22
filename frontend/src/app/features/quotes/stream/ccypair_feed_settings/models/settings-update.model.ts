/** DTO обновления настроек потока котировок аналогичный backend */
export interface SettingsUpdateDto {
  priority: number;
  refreshMs: number;
  enabled: boolean;
}
