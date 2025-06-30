import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse } from '../../../../shared/api-response.model';
import { SettingsDto } from '../models/settings.model';
import { SettingsCreateDto } from '../models/settings-create.model';
import { SettingsUpdateDto } from '../models/settings-update.model';

/* Сервис взаимодействия с API конфигураций стрима */
@Injectable({
  providedIn: 'root'
})
export class SettingsService {

  constructor(private http: HttpClient) { }

  /* Базовый URL API */
  private readonly baseUrl = '/api/v1/streaming-configs';

  /* Получить список всех настроек */
  list(): Observable<SettingsDto[]> {
    return this.http
      .get<ApiResponse<SettingsDto[]>>(this.baseUrl)
      .pipe(map(res => res.data));
  }

  /* Создать настройки, backend вернёт их id */
  add(dto: SettingsCreateDto): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(this.baseUrl, dto)
      .pipe(map(res => res.data));
  }

  /* Удалить настройки по паре и провайдеру */
  delete(pair: string, provider: string): Observable<void> {
    return this.http
      .delete<ApiResponse<void>>(`${this.baseUrl}/${pair}/${provider}`)
      .pipe(map(res => res.data));
  }

  /* Обновить настройки по паре и провайдеру */
  update(pair: string, provider: string, dto: SettingsUpdateDto): Observable<void> {
    return this.http
      .put<ApiResponse<void>>(`${this.baseUrl}/${pair}/${provider}`, dto)
      .pipe(map(res => res.data));
  }
}
