import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import {
  LiveQuoteMonitorRuntimeStatus,
  LiveQuoteMonitorRuntimeStatusResponseDto
} from '../models/quote-monitor-runtime.model';

@Injectable({
  providedIn: 'root'
})
export class QuoteMonitorRuntimeService {
  private readonly baseUrl = '/api/v1/quote-monitor/runtime';

  constructor(private readonly http: HttpClient) {}

  start(): Observable<LiveQuoteMonitorRuntimeStatus> {
    return this.http
      .post<LiveQuoteMonitorRuntimeStatusResponseDto>(`${this.baseUrl}/start`, {})
      .pipe(map(response => this.toRuntimeStatus(response)));
  }

  stop(): Observable<LiveQuoteMonitorRuntimeStatus> {
    return this.http
      .post<LiveQuoteMonitorRuntimeStatusResponseDto>(`${this.baseUrl}/stop`, {})
      .pipe(map(response => this.toRuntimeStatus(response)));
  }

  status(): Observable<LiveQuoteMonitorRuntimeStatus> {
    return this.http
      .get<LiveQuoteMonitorRuntimeStatusResponseDto>(`${this.baseUrl}/status`)
      .pipe(map(response => this.toRuntimeStatus(response)));
  }

  private toRuntimeStatus(
    response: LiveQuoteMonitorRuntimeStatusResponseDto
  ): LiveQuoteMonitorRuntimeStatus {
    return {
      status: response.status,
      monitoredInstrumentCodes: [...(response.monitoredInstrumentCodes ?? [])].sort((left, right) =>
        left.localeCompare(right)
      ),
      lastTickAt: response.lastTickAt ?? null
    };
  }
}
