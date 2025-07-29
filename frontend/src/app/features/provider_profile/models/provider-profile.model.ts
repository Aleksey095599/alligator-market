/** DTO для профиля провайдера, аналогичный backend. */
export interface ProviderProfileDto {
  providerCode: string;
  displayName: string;
  instrumentTypes: string[];
  deliveryMode: string;
  accessMethod: string;
  supportsBulkSubscription: boolean;
  minPollPeriodMs: number;
}
