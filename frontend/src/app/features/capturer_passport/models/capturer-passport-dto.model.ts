export interface CapturerPassportDto {
  capturerCode: string;
  displayName: string;
  registryStatus: 'REGISTERED' | 'RETIRED';
}
