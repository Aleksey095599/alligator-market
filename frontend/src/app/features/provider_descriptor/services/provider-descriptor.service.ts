import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse } from '../../../shared/api-response.model';
import { DescriptorEntityDto } from '../models/descriptor-entity.model';

/* Сервис для чтения дескрипторов провайдеров. */
@Injectable({
  providedIn: 'root'
})
export class ProviderDescriptorService {

  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/providers';

  /* Получить все дескрипторы провайдеров. */
  list(): Observable<DescriptorEntityDto[]> {
    return this.http
      .get<ApiResponse<DescriptorEntityDto[]>>(this.baseUrl)
      .pipe(map(res => res.data));
  }
}
