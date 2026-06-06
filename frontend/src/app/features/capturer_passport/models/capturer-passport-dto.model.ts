export interface CapturerPassportDto {
  capturerCode: string;
  displayName: string;
  description: string;
  registryStatus: 'REGISTERED' | 'RETIRED';
}
