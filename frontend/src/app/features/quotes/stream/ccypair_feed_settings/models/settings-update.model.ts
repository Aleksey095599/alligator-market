/** DTO обновления настроек потока котировок аналогичный backend */
export interface SettingsUpdateDto {
  priority: number;
  fetchPeriodMs: number;
  enabled: boolean;
}
