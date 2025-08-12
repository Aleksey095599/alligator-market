import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { ApiResponse } from '../../../shared/api-response.model';
import { FxOutrightDto } from '../models/fx-outright.model';

/* Сервис для чтения инструментов FX OUTRIGHT */
@Injectable({
  providedIn: 'root'
})
export class FxOutrightService {

  constructor(private http: HttpClient) { }

  /* Базовый URL (через proxy уйдёт на Spring) */
  private readonly baseUrl = '/api/v1/fx-outright-instrument';

  /* Получить список всех инструментов FX OUTRIGHT */
  list(): Observable<FxOutrightDto[]> {
    return this.http
      .get<ApiResponse<FxOutrightDto[]>>(this.baseUrl)
      .pipe(map(res => res.data));
  }
}
