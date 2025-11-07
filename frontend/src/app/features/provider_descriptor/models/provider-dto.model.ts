/**
 * DTO провайдера, соответствующее backend-контракту ProviderDto.
 */
export interface ProviderDto {
  /* Технический код провайдера. */
  providerCode: string;
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
