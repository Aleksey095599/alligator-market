import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import {
  SourcePlanListResponseDto,
  SourcePlanResponseDto
} from '../models/source-plan.model';
import { SourcePlanOptionsResponseDto } from '../models/source-plan-options.model';
import { CreateSourcePlanDto } from '../models/create-source-plan.model';
import { ReplaceSourcePlanDto } from '../models/replace-source-plan.model';

/* Сервис для управления whole-plan sourcing планами инструмента. */
@Injectable({
  providedIn: 'root'
})
export class SourcePlanService {

  constructor(private readonly http: HttpClient) {}

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/source-plans';

  /* Получить все source plans. */
  list(): Observable<SourcePlanResponseDto[]> {
    return this.http
      .get<SourcePlanListResponseDto>(this.baseUrl)
      .pipe(map(res => res.plans ?? []));
  }

  /* Получить whole-plan по capturerCode и instrumentCode. */
  get(
    capturerCode: string,
    instrumentCode: string
  ): Observable<SourcePlanResponseDto> {
    return this.http.get<SourcePlanResponseDto>(
      `${this.baseUrl}/${this.pathSegment(capturerCode)}/${this.pathSegment(instrumentCode)}`
    );
  }

  /* Получить опции инструментов и источников для формы. */
  getOptions(): Observable<SourcePlanOptionsResponseDto> {
    return this.http.get<SourcePlanOptionsResponseDto>(`${this.baseUrl}/options`);
  }

  /* Создать новый whole-plan. */
  create(dto: CreateSourcePlanDto): Observable<void> {
    return this.http
      .post<void>(this.baseUrl, dto)
      .pipe(map(() => undefined));
  }

  /* Полностью заменить whole-plan по capturerCode и instrumentCode. */
  replace(
    capturerCode: string,
    instrumentCode: string,
    dto: ReplaceSourcePlanDto
  ): Observable<void> {
    return this.http
      .put<void>(
        `${this.baseUrl}/${this.pathSegment(capturerCode)}/${this.pathSegment(instrumentCode)}`,
        dto
      )
      .pipe(map(() => undefined));
  }

  /* Удалить whole-plan по capturerCode и instrumentCode. */
  delete(capturerCode: string, instrumentCode: string): Observable<void> {
    return this.http
      .delete<void>(
        `${this.baseUrl}/${this.pathSegment(capturerCode)}/${this.pathSegment(instrumentCode)}`
      )
      .pipe(map(() => undefined));
  }

  private pathSegment(value: string): string {
    return encodeURIComponent(value);
  }
}
