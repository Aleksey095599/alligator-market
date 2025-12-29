/* Контракт универсального ответа backend'а. */
export interface ApiResponse<T> {
  data: T | null;
  success: boolean;
  errorCode: string | null;
  message: string;
  timestamp: string;
}
