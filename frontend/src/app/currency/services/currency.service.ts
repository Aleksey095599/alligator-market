import { Injectable } from '@angular/core';
import {CurrencyDto} from '../models/currency.model';
import {map, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {ApiResponse} from '../../shared/api-response.model';

/* Сервис для взаимодействия с API по работе с валютами */
@Injectable({
  providedIn: 'root'
})
export class CurrencyService {


  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring) */
  private readonly baseUrl = '/api/v1/currencies';

  /* Получить список всех валют */
  list(): Observable<CurrencyDto[]> {
    return this.http
      .get<ApiResponse<CurrencyDto[]>>(this.baseUrl)
      .pipe(map(res => res.data));
  }

  /* Добавить валюту, backend вернёт её код */
  add(dto: CurrencyDto): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(this.baseUrl, dto)
      .pipe(map(res => res.data));
  }

  /* Удалить валюту по коду */
  delete(code: string): Observable<void> {
    return this.http
      .delete<ApiResponse<void>>(`${this.baseUrl}/${code}`)
      .pipe(map(res => res.data));
  }

}
