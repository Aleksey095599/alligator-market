export interface SourcePassportDto {
  sourceCode: string;
  displayName: string;
  description: string;
  registryStatus: 'REGISTERED' | 'RETIRED';
}
