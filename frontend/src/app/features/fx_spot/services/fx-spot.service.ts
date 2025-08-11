import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse } from '../../../shared/api-response.model';
import { FxSpotDto } from '../models/fx-spot.model';

/* Сервис для чтения инструментов FX SPOT */
@Injectable({
  providedIn: 'root'
})
export class FxSpotService {

  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring) */
  private readonly baseUrl = '/api/v1/fx-spots';

  /* Получить список всех инструментов FX SPOT */
  list(): Observable<FxSpotDto[]> {
    return this.http
      .get<ApiResponse<FxSpotDto[]>>(this.baseUrl)
      .pipe(map(res => res.data));
  }
}
