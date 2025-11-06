/**
 * DTO дескриптора провайдера, соответствующее backend-контракту DescriptorDto.
 * Код провайдера backend может возвращать дополнительно, но на UI он не используется.
 */
export interface DescriptorDto {
  /* Отображаемое имя провайдера (user friendly). */
  displayName: string;
  /* Режим доставки рыночных данных. */
  deliveryMode: string;
  /* Метод доступа к данным. */
  accessMethod: string;
  /* Признак поддержки массовой подписки. */
  bulkSubscription: boolean;
  /* Минимальный интервал обновления данных в секундах. */
  minUpdateIntervalSeconds: number;
}
