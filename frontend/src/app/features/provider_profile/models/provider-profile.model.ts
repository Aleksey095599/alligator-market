/** DTO для профиля провайдера, аналогичный backend. */
export interface ProviderProfileDto {
  providerCode: string;
  displayName: string;
  instrumentsSupported: string[];
  deliveryMode: string;
  accessMethod: string;
  bulkSubscription: boolean;
  minPollMs: number;
}
