import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import { ApiResponse } from '../../../shared/api/api-response.model';
import { ProviderDto } from '../models/provider-dto.model';

/* Сервис для чтения дескрипторов провайдеров. */
@Injectable({
  providedIn: 'root'
})
export class ProviderDescriptorService {

  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/providers';

  /* Получить все дескрипторы провайдеров. */
  list(): Observable<ProviderDto[]> {
    return this.http
      .get<ApiResponse<ProviderDto[]>>(this.baseUrl)
      .pipe(map(res => res.data ?? []));
  }
}
