import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import {
  MarketDataSourcePlanListResponseDto,
  MarketDataSourcePlanResponseDto
} from '../models/market-data-source-plan.model';
import { MarketDataSourcePlanOptionsResponseDto } from '../models/market-data-source-plan-options.model';
import { CreateMarketDataSourcePlanDto } from '../models/create-market-data-source-plan.model';
import { ReplaceMarketDataSourcePlanDto } from '../models/replace-market-data-source-plan.model';

/* Сервис для управления whole-plan sourcing планами инструмента. */
@Injectable({
  providedIn: 'root'
})
export class MarketDataSourcePlanService {

  constructor(private readonly http: HttpClient) {}

  /* Базовый URL (через proxy уйдёт на Spring). */
  private readonly baseUrl = '/api/v1/market-data-source-plans';

  /* Получить все sourcing plans. */
  list(): Observable<MarketDataSourcePlanResponseDto[]> {
    return this.http
      .get<MarketDataSourcePlanListResponseDto>(this.baseUrl)
      .pipe(map(res => res.plans ?? []));
  }

  /* Получить whole-plan по captureProcessCode и instrumentCode. */
  get(
    captureProcessCode: string,
    instrumentCode: string
  ): Observable<MarketDataSourcePlanResponseDto> {
    return this.http.get<MarketDataSourcePlanResponseDto>(
      `${this.baseUrl}/${this.pathSegment(captureProcessCode)}/${this.pathSegment(instrumentCode)}`
    );
  }

  /* Получить опции инструментов и провайдеров для формы. */
  getOptions(): Observable<MarketDataSourcePlanOptionsResponseDto> {
    return this.http.get<MarketDataSourcePlanOptionsResponseDto>(`${this.baseUrl}/options`);
  }

  /* Создать новый whole-plan. */
  create(dto: CreateMarketDataSourcePlanDto): Observable<void> {
    return this.http
      .post<void>(this.baseUrl, dto)
      .pipe(map(() => undefined));
  }

  /* Полностью заменить whole-plan по captureProcessCode и instrumentCode. */
  replace(
    captureProcessCode: string,
    instrumentCode: string,
    dto: ReplaceMarketDataSourcePlanDto
  ): Observable<void> {
    return this.http
      .put<void>(
        `${this.baseUrl}/${this.pathSegment(captureProcessCode)}/${this.pathSegment(instrumentCode)}`,
        dto
      )
      .pipe(map(() => undefined));
  }

  /* Удалить whole-plan по captureProcessCode и instrumentCode. */
  delete(captureProcessCode: string, instrumentCode: string): Observable<void> {
    return this.http
      .delete<void>(
        `${this.baseUrl}/${this.pathSegment(captureProcessCode)}/${this.pathSegment(instrumentCode)}`
      )
      .pipe(map(() => undefined));
  }

  private pathSegment(value: string): string {
    return encodeURIComponent(value);
  }
}
