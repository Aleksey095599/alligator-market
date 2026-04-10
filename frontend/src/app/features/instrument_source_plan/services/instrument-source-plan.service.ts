import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import { ApiResponse } from '../../../shared/api/api-response.model';
import {
  InstrumentSourcePlanListResponseDto,
  InstrumentSourcePlanResponseDto
} from '../models/instrument-source-plan.model';
import { InstrumentSourcePlanOptionsResponseDto } from '../models/instrument-source-plan-options.model';
import { CreateInstrumentSourcePlanDto } from '../models/create-instrument-source-plan.model';
import { ReplaceInstrumentSourcePlanDto } from '../models/replace-instrument-source-plan.model';

/* Сервис для управления whole-plan sourcing планами инструмента. */
@Injectable({
  providedIn: 'root'
})
export class InstrumentSourcePlanService {

  constructor(private readonly http: HttpClient) {}

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/instrument-source-plans';

  /* Получить все sourcing plans. */
  list(): Observable<InstrumentSourcePlanResponseDto[]> {
    return this.http
      .get<ApiResponse<InstrumentSourcePlanListResponseDto>>(this.baseUrl)
      .pipe(map(res => res.data?.plans ?? []));
  }

  /* Получить whole-plan по instrumentCode. */
  get(instrumentCode: string): Observable<InstrumentSourcePlanResponseDto> {
    return this.http
      .get<ApiResponse<InstrumentSourcePlanResponseDto>>(`${this.baseUrl}/${instrumentCode}`)
      .pipe(map(res => res.data as InstrumentSourcePlanResponseDto));
  }

  /* Получить опции инструментов и провайдеров для формы. */
  getOptions(): Observable<InstrumentSourcePlanOptionsResponseDto> {
    return this.http
      .get<ApiResponse<InstrumentSourcePlanOptionsResponseDto>>(`${this.baseUrl}/options`)
      .pipe(
        map(res => res.data ?? { instruments: [], providers: [] })
      );
  }

  /* Создать новый whole-plan. */
  create(dto: CreateInstrumentSourcePlanDto): Observable<void> {
    return this.http
      .post<void>(this.baseUrl, dto)
      .pipe(map(() => undefined));
  }

  /* Полностью заменить whole-plan по instrumentCode. */
  replace(instrumentCode: string, dto: ReplaceInstrumentSourcePlanDto): Observable<void> {
    return this.http
      .put<void>(`${this.baseUrl}/${instrumentCode}`, dto)
      .pipe(map(() => undefined));
  }

  /* Удалить whole-plan по instrumentCode. */
  delete(instrumentCode: string): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${instrumentCode}`)
      .pipe(map(() => undefined));
  }
}
