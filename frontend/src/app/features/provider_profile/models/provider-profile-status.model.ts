export interface ProviderProfileStatusDto {
  providerCode: string;
  displayName: string;
  instrumentTypes: string[];
  deliveryMode: string;
  accessMethod: string;
  bulkSubscription: boolean;
  minPollMs: number;
  status: string;
}
