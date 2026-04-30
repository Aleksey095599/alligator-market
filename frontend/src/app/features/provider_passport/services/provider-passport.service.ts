import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ProviderDto } from '../models/provider-dto.model';

/* Сервис для чтения паспортов провайдеров. */
@Injectable({
  providedIn: 'root'
})
export class ProviderPassportService {

  constructor(private http: HttpClient) {}

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/providers';

  /* Получить все паспорта провайдеров. */
  list(): Observable<ProviderDto[]> {
    return this.http.get<ProviderDto[]>(this.baseUrl);
  }
}
