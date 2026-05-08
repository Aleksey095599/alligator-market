import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { MarketDataCapturerPassportDto } from '../models/market-data-capturer-passport-dto.model';

/* Сервис для чтения паспортов процессов захвата. */
@Injectable({
  providedIn: 'root'
})
export class MarketDataCapturerPassportService {

  constructor(private http: HttpClient) {}

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/market-data-capturers';

  /* Получить все паспорта процессов захвата. */
  list(): Observable<MarketDataCapturerPassportDto[]> {
    return this.http.get<MarketDataCapturerPassportDto[]>(this.baseUrl);
  }
}
