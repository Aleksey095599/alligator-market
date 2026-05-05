/**
 * DTO паспорта процесса фиксации, соответствующее backend-контракту.
 */
export interface MDCaptureProcessPassportDto {
  /* Уникальный код процесса фиксации. */
  captureProcessCode: string;
  /* Отображаемое имя процесса фиксации. */
  displayName: string;
}
