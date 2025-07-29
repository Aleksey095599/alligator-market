import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse } from '../../../shared/api-response.model';
import { ProviderProfileDto } from '../models/provider-profile.model';
import { ProviderProfileStatusDto } from '../models/provider-profile-status.model';

/* Сервис для чтения профилей провайдеров */
@Injectable({
  providedIn: 'root'
})
export class ProviderProfileService {

  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring) */
  private readonly baseUrl = '/api/v1/providers';

  /* Получить список всех профилей */
  list(): Observable<ProviderProfileDto[]> {
    return this.http
      .get<ApiResponse<ProviderProfileDto[]>>(this.baseUrl)
      .pipe(map(res => res.data));
  }

  /* Получить список всех профилей со статусами */
  listAll(): Observable<ProviderProfileStatusDto[]> {
    return this.http
      .get<ApiResponse<ProviderProfileStatusDto[]>>(`${this.baseUrl}/audit`)
      .pipe(map(res => res.data));
  }
}
