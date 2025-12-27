import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import { CreateCurrencyDto, CurrencyResponseDto } from '../models/currency.model';
import { ApiResponse } from '../../../shared/api/api-response.model';
import { UpdateCurrencyDto } from '../models/currency-update.model';

/* Сервис для взаимодействия с API по работе с валютами. */
@Injectable({
  providedIn: 'root'
})
export class CurrencyService {

  constructor(private http: HttpClient) {}

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/currencies';

  /* Получить список всех валют. */
  list(): Observable<CurrencyResponseDto[]> {
    return this.http
      .get<ApiResponse<CurrencyResponseDto[]>>(this.baseUrl)
      .pipe(map(res => res.data ?? []));
  }

  /* Добавить валюту, backend вернёт её код. */
  add(dto: CreateCurrencyDto): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(this.baseUrl, dto)
      .pipe(map(res => res.data ?? ''));
  }

  /* Удалить валюту по коду. */
  delete(code: string): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${code}`)
      .pipe(map(() => undefined));
  }

  /* Обновить валюту по коду. */
  update(code: string, dto: UpdateCurrencyDto): Observable<void> {
    return this.http
      .put<void>(`${this.baseUrl}/${code}`, dto)
      .pipe(map(() => undefined));
  }
}
