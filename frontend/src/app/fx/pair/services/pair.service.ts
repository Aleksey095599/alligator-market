import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse } from '../../../shared/api-response.model';
import { PairDto } from '../models/pair.model';
import { PairUpdateDto } from '../models/pair-update.model';

/* Сервис для взаимодействия с API по работе с валютными парами */
@Injectable({
  providedIn: 'root'
})
export class PairService {

  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring) */
  private readonly baseUrl = '/api/v1/pairs';

  /* Получить список всех валютных пар */
  list(): Observable<PairDto[]> {
    return this.http
      .get<ApiResponse<PairDto[]>>(this.baseUrl)
      .pipe(map(res => res.data));
  }

  /* Добавить валютную пару, backend вернёт её pair */
  add(dto: PairDto): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(this.baseUrl, dto)
      .pipe(map(res => res.data));
  }

  /* Удалить валютную пару по коду pair */
  delete(pair: string): Observable<void> {
    return this.http
      .delete<ApiResponse<void>>(`${this.baseUrl}/${pair}`)
      .pipe(map(res => res.data));
  }

  /* Обновить валютную пару по коду pair */
  update(pair: string, dto: PairUpdateDto): Observable<void> {
    return this.http
      .put<ApiResponse<void>>(`${this.baseUrl}/${pair}`, dto)
      .pipe(map(res => res.data));
  }
}
