import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { MDCaptureProcessPassportDto } from '../models/md-capture-process-passport-dto.model';

/* Сервис для чтения паспортов процессов фиксации. */
@Injectable({
  providedIn: 'root'
})
export class MDCaptureProcessPassportService {

  constructor(private http: HttpClient) {}

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/market-data-capture-processes';

  /* Получить все паспорта процессов фиксации. */
  list(): Observable<MDCaptureProcessPassportDto[]> {
    return this.http.get<MDCaptureProcessPassportDto[]>(this.baseUrl);
  }
}
