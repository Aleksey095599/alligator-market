/* Единый ответ backend'а */
export interface ApiResponse<T> {
  data: T;
  message: string;
  timestamp: string;
}
