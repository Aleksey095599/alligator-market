export interface MarketDataSourcePassportDto {
  sourceCode: string;
  displayName: string;
  deliveryMode: string;
  accessMethod: string;
  bulkSubscription: boolean;
  lifecycleStatus: 'ACTIVE' | 'RETIRED';
}
