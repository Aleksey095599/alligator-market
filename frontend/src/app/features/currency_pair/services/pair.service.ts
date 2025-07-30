import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse } from '../../../shared/api-response.model';
import { PairDto } from '../models/pair.model';
import { PairCreateDto } from '../models/pair-create.model';
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

  /* Добавить валютную пару, backend вернёт её pairCode */
  add(dto: PairCreateDto): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(this.baseUrl, dto)
      .pipe(map(res => res.data));
  }

  /* Удалить валютную пару по коду pairCode */
  delete(pairCode: string): Observable<void> {
    const base = pairCode.substring(0, 3);
    const quote = pairCode.substring(3, 6);
    return this.http
      .delete<ApiResponse<void>>(`${this.baseUrl}/${base}/${quote}`)
      .pipe(map(res => res.data));
  }

  /* Обновить валютную пару по коду pairCode */
  update(pairCode: string, dto: PairUpdateDto): Observable<void> {
    const base = pairCode.substring(0, 3);
    const quote = pairCode.substring(3, 6);
    return this.http
      .put<ApiResponse<void>>(`${this.baseUrl}/${base}/${quote}`, dto)
      .pipe(map(res => res.data));
  }
}
