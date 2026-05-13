export interface SourcePassportDto {
  sourceCode: string;
  displayName: string;
  deliveryMode: string;
  accessMethod: string;
  lifecycleStatus: 'ACTIVE' | 'RETIRED';
}
