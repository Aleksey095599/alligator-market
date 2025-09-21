/** DTO дескриптора провайдера, соответствующее backend-контракту DescriptorDto. */
export interface DescriptorDto {
  providerCode: string;
  displayName: string;
  deliveryMode: string;
  accessMethod: string;
  bulkSubscription: boolean;
}
