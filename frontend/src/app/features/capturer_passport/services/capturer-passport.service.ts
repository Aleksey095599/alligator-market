import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { CapturerPassportDto } from '../models/capturer-passport-dto.model';

/* Сервис для чтения паспортов процессов захвата. */
@Injectable({
  providedIn: 'root'
})
export class CapturerPassportService {

  constructor(private http: HttpClient) {}

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/capturers';

  /* Получить все паспорта процессов захвата. */
  list(): Observable<CapturerPassportDto[]> {
    return this.http.get<CapturerPassportDto[]>(this.baseUrl);
  }
}
