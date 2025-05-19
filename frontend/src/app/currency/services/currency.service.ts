import { Injectable } from '@angular/core';
import {CurrencyDto} from '../models/currency.model';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class CurrencyService {

  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring) */
  private readonly baseUrl = '/api/v1/currencies';

  /* Получить все валюты */
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
