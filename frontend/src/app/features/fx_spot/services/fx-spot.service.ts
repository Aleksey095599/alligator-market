import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse } from '../../../shared/api-response.model';
import { FxSpotCreateDto } from '../models/fx-spot-create.model';
import { FxSpotDto, FxSpotItemDto } from '../models/fx-spot.model';
import { FxSpotUpdateDto } from '../models/fx-spot-update.model';

/* Сервис для взаимодействия с API по работе с инструментами FX_SPOT */
@Injectable({
  providedIn: 'root'
})
export class FxSpotService {

  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring) */
  private readonly baseUrl = '/api/v1/fx-spot';

  /* Получить список всех инструментов FX_SPOT */
  list(): Observable<FxSpotItemDto[]> {
    return this.http
      .get<ApiResponse<FxSpotDto[]>>(this.baseUrl)
      .pipe(map(res => res.data.map(dto => ({
        ...dto,
        instrumentCode: this.buildCode(dto)
      }))));
  }

  /* Добавить инструмент FX_SPOT, backend вернёт его код */
  add(dto: FxSpotCreateDto): Observable<string> {
    return this.http
      .post<ApiResponse<string>>(this.baseUrl, dto)
      .pipe(map(res => res.data));
  }

  /* Удалить инструмент FX_SPOT по коду */
  delete(instrumentCode: string): Observable<void> {
    return this.http
      .delete<ApiResponse<void>>(`${this.baseUrl}/${instrumentCode}`)
      .pipe(map(res => res.data));
  }

  /* Обновить инструмент FX_SPOT по коду */
  update(instrumentCode: string, dto: FxSpotUpdateDto): Observable<void> {
    return this.http
      .patch<ApiResponse<void>>(`${this.baseUrl}/${instrumentCode}`, dto)
      .pipe(map(res => res.data));
  }

  /* Построить код инструмента из данных DTO */
  private buildCode(dto: FxSpotDto): string {
    return `${dto.baseCurrency}${dto.quoteCurrency}_${dto.valueDateCode}`;
  }
}
