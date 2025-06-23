import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse } from '../../../shared/api-response.model';
import { ProviderDto } from '../models/provider.model';
import { ProviderCreateDto } from '../models/provider-create.model';
import { ProviderUpdateDto } from '../models/provider-update.model';

/* Сервис для взаимодействия с API по работе с провайдерами */
@Injectable({
  providedIn: 'root'
})
export class ProviderService {

  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring) */
  private readonly baseUrl = '/api/v1/providers';

  /* Получить список всех провайдеров */
  list(): Observable<ProviderDto[]> {
    return this.http
      .get<ApiResponse<ProviderDto[]>>(this.baseUrl)
      .pipe(map(res => res.data));
  }

  /* Добавить провайдера, backend вернёт его name */
  add(dto: ProviderCreateDto): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(this.baseUrl, dto)
      .pipe(map(res => res.data));
  }

  /* Удалить провайдера по имени */
  delete(name: string): Observable<void> {
    return this.http
      .delete<ApiResponse<void>>(`${this.baseUrl}/${name}`)
      .pipe(map(res => res.data));
  }

  /* Обновить провайдера по имени */
  update(name: string, dto: ProviderUpdateDto): Observable<void> {
    return this.http
      .put<ApiResponse<void>>(`${this.baseUrl}/${name}`, dto)
      .pipe(map(res => res.data));
  }
}
