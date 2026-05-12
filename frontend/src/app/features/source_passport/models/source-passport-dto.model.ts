export interface SourcePassportDto {
  sourceCode: string;
  displayName: string;
  deliveryMode: string;
  accessMethod: string;
  bulkSubscription: boolean;
  lifecycleStatus: 'ACTIVE' | 'RETIRED';
}
