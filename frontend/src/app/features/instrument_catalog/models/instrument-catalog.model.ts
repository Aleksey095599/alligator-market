export interface InstrumentAttributeDto {
  key: string;
  label: string;
  value: string;
}

export interface InstrumentCatalogItemDto {
  instrumentCode: string;
  displayName: string;
  asset: string;
  product: string;
  description: string;
  attributes: InstrumentAttributeDto[];
}

export interface InstrumentAttribute {
  key: string;
  label: string;
  value: string;
}

export interface InstrumentCatalogItem {
  instrumentCode: string;
  displayName: string;
  asset: string;
  product: string;
  description: string;
  attributes: InstrumentAttribute[];
}
