export interface MarketDataCapturerPassportDto {
  capturerCode: string;
  displayName: string;
  lifecycleStatus: 'ACTIVE' | 'RETIRED';
}
