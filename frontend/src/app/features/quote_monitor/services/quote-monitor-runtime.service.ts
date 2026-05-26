import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import {
  QuoteMonitorInstrumentRuntimeState,
  QuoteMonitorRuntimeStatus,
  QuoteMonitorRuntimeStatusResponseDto
} from '../models/quote-monitor-runtime.model';

@Injectable({
  providedIn: 'root'
})
export class QuoteMonitorRuntimeService {
  private readonly baseUrl = '/api/v1/quote-monitor/runtime';

  constructor(private readonly http: HttpClient) {}

  start(): Observable<QuoteMonitorRuntimeStatus> {
    return this.http
      .post<QuoteMonitorRuntimeStatusResponseDto>(`${this.baseUrl}/start`, {})
      .pipe(map(response => this.toRuntimeStatus(response)));
  }

  stop(): Observable<QuoteMonitorRuntimeStatus> {
    return this.http
      .post<QuoteMonitorRuntimeStatusResponseDto>(`${this.baseUrl}/stop`, {})
      .pipe(map(response => this.toRuntimeStatus(response)));
  }

  status(): Observable<QuoteMonitorRuntimeStatus> {
    return this.http
      .get<QuoteMonitorRuntimeStatusResponseDto>(`${this.baseUrl}/status`)
      .pipe(map(response => this.toRuntimeStatus(response)));
  }

  private toRuntimeStatus(
    response: QuoteMonitorRuntimeStatusResponseDto
  ): QuoteMonitorRuntimeStatus {
    return {
      status: response.status,
      monitoredInstrumentCodes: [...(response.monitoredInstrumentCodes ?? [])].sort((left, right) =>
        left.localeCompare(right)
      ),
      lastTickAt: response.lastTickAt ?? null,
      instrumentStates: this.toInstrumentStates(response)
    };
  }

  private toInstrumentStates(
    response: QuoteMonitorRuntimeStatusResponseDto
  ): QuoteMonitorInstrumentRuntimeState[] {
    return [...(response.instrumentStates ?? [])]
      .map(state => ({
        instrumentCode: state.instrumentCode,
        sourceCode: state.sourceCode ?? null,
        status: state.status,
        detail: state.detail ?? null
      }))
      .sort((left, right) => left.instrumentCode.localeCompare(right.instrumentCode));
  }
}
