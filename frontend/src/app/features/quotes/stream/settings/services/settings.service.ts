import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse } from '../../../../../shared/api-response.model';
import { StreamConfigDto } from '../models/settings.model';
import { StreamConfigCreateDto } from '../models/settings-create.model';
import { StreamConfigUpdateDto } from '../models/settings-update.model';

/* Сервис взаимодействия с API конфигураций стрима */
@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  constructor(private http: HttpClient) { }

  /* Базовый URL API */
  private readonly baseUrl = '/api/v1/streaming-configs';

  /* Получить список всех конфигураций */
  list(): Observable<StreamConfigDto[]> {
    return this.http
      .get<ApiResponse<StreamConfigDto[]>>(this.baseUrl)
      .pipe(map(res => res.data));
  }

  /* Создать конфигурацию, backend вернёт её id */
  add(dto: StreamConfigCreateDto): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(this.baseUrl, dto)
      .pipe(map(res => res.data));
  }

  /* Удалить конфигурацию по паре и провайдеру */
  delete(pair: string, provider: string): Observable<void> {
    return this.http
      .delete<ApiResponse<void>>(`${this.baseUrl}/${pair}/${provider}`)
      .pipe(map(res => res.data));
  }

  /* Обновить конфигурацию по паре и провайдеру */
  update(pair: string, provider: string, dto: StreamConfigUpdateDto): Observable<void> {
    return this.http
      .put<ApiResponse<void>>(`${this.baseUrl}/${pair}/${provider}`, dto)
      .pipe(map(res => res.data));
  }
}
