import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse } from '../../../shared/api-response.model';
import { DescriptorDto } from '../models/descriptor-dto.model';

/* Сервис для чтения дескрипторов провайдеров. */
@Injectable({
  providedIn: 'root'
})
export class ProviderDescriptorService {

  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/providers';

  /* Получить все дескрипторы провайдеров. */
  list(): Observable<DescriptorDto[]> {
    return this.http
      .get<ApiResponse<DescriptorDto[]>>(this.baseUrl)
      .pipe(map(res => res.data));
  }
}
