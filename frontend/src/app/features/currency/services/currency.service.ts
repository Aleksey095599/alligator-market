import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { CreateCurrencyDto, CurrencyResponseDto } from '../models/currency.model';
import { UpdateCurrencyDto } from '../models/currency-update.model';

/* Сервис для взаимодействия с API по работе с валютами. */
@Injectable({
  providedIn: 'root'
})
export class CurrencyService {

  constructor(private http: HttpClient) {}

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/currencies';

  /* Получить список всех валют в стандартном Spring-формате ответа. */
  list(): Observable<CurrencyResponseDto[]> {
    return this.http.get<CurrencyResponseDto[]>(this.baseUrl);
  }

  /* Добавить валюту, backend вернёт её код plain string. */
  add(dto: CreateCurrencyDto): Observable<string> {
    return this.http.post(this.baseUrl, dto, { responseType: 'text' });
  }

  /* Удалить валюту по коду. */
  delete(code: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${code}`);
  }

  /* Обновить валюту по коду. */
  update(code: string, dto: UpdateCurrencyDto): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${code}`, dto);
  }
}
