export interface ProviderProfileStatusDto {
  providerCode: string;
  displayName: string;
  instrumentTypes: string[];
  deliveryMode: string;
  accessMethod: string;
  supportsBulkSubscription: boolean;
  minPollPeriodMs: number;
  status: string;
}
