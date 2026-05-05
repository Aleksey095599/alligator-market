export interface MarketDataCaptureProcessPassportDto {
  captureProcessCode: string;
  displayName: string;
  lifecycleStatus: 'ACTIVE' | 'RETIRED';
}
