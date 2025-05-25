import { Injectable } from '@angular/core';
import {CurrencyDto} from '../models/currency.model';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

/* Сервис для взаимодействия с API по работе с валютами */
@Injectable({
  providedIn: 'root'
})
export class CurrencyService {


  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring) */
  private readonly baseUrl = '/api/v1/currencies';

  /* Получить все валюты
   * Выполняет HTTP GET запрос к /api/v1/currencies
   * @returns Observable массива валют CurrencyDto[] с сервера
   * Observable позволяет асинхронно получить данные когда они будут готовы */
  list(): Observable<CurrencyDto[]> {
    return this.http.get<CurrencyDto[]>(this.baseUrl);
  }

  /* Добавить валюту, backend вернёт её код */
  add(dto: CurrencyDto): Observable<string> {
    return this.http.post<string>(this.baseUrl, dto);
  }

  /* Удалить валюту по коду */
  delete(code: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${code}`);
  }
}
