export interface ProviderDto {
  providerCode: string;
  displayName: string;
  deliveryMode: string;
  accessMethod: string;
  bulkSubscription: boolean;
  lifecycleStatus: 'ACTIVE' | 'RETIRED';
}
