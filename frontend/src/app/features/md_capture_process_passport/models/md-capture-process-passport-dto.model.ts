/**
 * DTO паспорта процесса захвата, соответствующее backend-контракту.
 */
export interface MDCaptureProcessPassportDto {
  /* Уникальный код процесса захвата. */
  captureProcessCode: string;
  /* Отображаемое имя процесса захвата. */
  displayName: string;
}
