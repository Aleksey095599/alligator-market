import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { MarketDataCaptureProcessPassportDto } from '../models/market-data-capture-process-passport-dto.model';

/* Сервис для чтения паспортов процессов захвата. */
@Injectable({
  providedIn: 'root'
})
export class MarketDataCaptureProcessPassportService {

  constructor(private http: HttpClient) {}

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/market-data-capture-processes';

  /* Получить все паспорта процессов захвата. */
  list(): Observable<MarketDataCaptureProcessPassportDto[]> {
    return this.http.get<MarketDataCaptureProcessPassportDto[]>(this.baseUrl);
  }
}
