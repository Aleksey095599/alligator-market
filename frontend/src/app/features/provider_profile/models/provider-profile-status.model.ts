export interface ProviderProfileStatusDto {
  providerCode: string;
  displayName: string;
  instrumentsSupported: string[];
  deliveryMode: string;
  accessMethod: string;
  bulkSubscription: boolean;
  minPollMs: number;
  status: string;
}
