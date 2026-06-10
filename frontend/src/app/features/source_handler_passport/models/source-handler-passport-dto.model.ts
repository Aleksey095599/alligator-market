export type SourceHandlerPassportRegistryStatus = 'REGISTERED' | 'RETIRED';

export interface SourceHandlerPassportDto {
  sourceCode: string;
  handlerCode: string;
  deliveryMode: string;
  accessMethod: string;
  registryStatus: SourceHandlerPassportRegistryStatus;
}
