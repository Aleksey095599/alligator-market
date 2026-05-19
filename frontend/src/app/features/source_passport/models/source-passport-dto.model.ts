export interface SourcePassportDto {
  sourceCode: string;
  displayName: string;
  lifecycleStatus: 'ACTIVE' | 'RETIRED';
}
