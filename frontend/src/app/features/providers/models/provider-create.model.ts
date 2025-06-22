/** DTO для создания провайдера аналогичный backend */
export interface ProviderCreateDto {
  name: string;
  baseUrl: string;
  mode: string;
  apiKey: string;
}
