import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import { ApiResponse } from '../../../shared/api/api-response.model';
import { FxSpotCreateDto } from '../models/fx-spot-create.model';
import { FxSpotListItemDto } from '../models/fx-spot.model';
import { FxSpotUpdateDto } from '../models/fx-spot-update.model';

/* Сервис для взаимодействия с API по работе с инструментами FX_SPOT. */
@Injectable({
  providedIn: 'root'
})
export class FxSpotService {

  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/fx-spot';

  /* Получить список всех инструментов FX_SPOT. */
  list(): Observable<FxSpotListItemDto[]> {
    return this.http
      .get<ApiResponse<FxSpotListItemDto[]>>(this.baseUrl)
      .pipe(map(res => res.data ?? []));
  }

  /* Добавить инструмент FX_SPOT, backend вернёт его код. */
  add(dto: FxSpotCreateDto): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(this.baseUrl, dto)
      .pipe(map(res => res.data ?? ''));
  }

  /* Удалить инструмент FX_SPOT по коду. */
  delete(code: string): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${code}`)
      .pipe(map(() => undefined));
  }

  /* Обновить инструмент FX_SPOT по коду. */
  update(code: string, dto: FxSpotUpdateDto): Observable<void> {
    return this.http
      .patch<void>(`${this.baseUrl}/${code}`, dto)
      .pipe(map(() => undefined));
  }
}
