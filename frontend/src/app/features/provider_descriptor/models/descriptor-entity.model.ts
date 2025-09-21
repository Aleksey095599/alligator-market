/** DTO дескриптора провайдера, повторяющий поля DescriptorEntity. */
export interface DescriptorEntityDto {
  providerCode: string;
  displayName: string;
  deliveryMode: string;
  accessMethod: string;
  bulkSubscription: boolean;
}
