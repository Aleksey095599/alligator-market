/** DTO обновления настроек потока котировок аналогичный backend */
export interface SettingsUpdateDto {
  priority: number;
  fetchPeriodMs: number; // минимум 1000 или 0 для PUSH
  enabled: boolean;
}
