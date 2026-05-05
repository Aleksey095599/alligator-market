/**
 * DTO паспорта процесса захвата, соответствующее backend-контракту.
 */
export interface MarketDataCaptureProcessPassportDto {
  /* Уникальный код процесса захвата. */
  captureProcessCode: string;
  /* Отображаемое имя процесса захвата. */
  displayName: string;
}
