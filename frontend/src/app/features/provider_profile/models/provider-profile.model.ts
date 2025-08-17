/** DTO для профиля провайдера, аналогичный backend. */
export interface ProviderProfileDto {
  providerCode: string;
  displayName: string;
  instrumentTypes: string[];
  deliveryMode: string;
  accessMethod: string;
  bulkSubscription: boolean;
  minPollMs: number;
}
