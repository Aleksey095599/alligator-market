export interface SourcePassportDto {
  sourceCode: string;
  displayName: string;
  registryStatus: 'REGISTERED' | 'RETIRED';
}
