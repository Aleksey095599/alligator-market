export interface CapturerPassportDto {
  capturerCode: string;
  displayName: string;
  lifecycleStatus: 'ACTIVE' | 'RETIRED';
}
