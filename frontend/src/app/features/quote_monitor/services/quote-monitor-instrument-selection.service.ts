import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import {
  QuoteMonitorInstrumentOption,
  QuoteMonitorInstrumentOptionsResponseDto,
  QuoteMonitorInstrumentSelectionResponseDto,
  ReplaceQuoteMonitorInstrumentSelectionDto
} from '../models/quote-monitor-instrument-selection.model';

@Injectable({
  providedIn: 'root'
})
export class QuoteMonitorInstrumentSelectionService {
  private readonly baseUrl = '/api/v1/quote-monitor/instrument-selection';

  constructor(private readonly http: HttpClient) {}

  getOptions(): Observable<QuoteMonitorInstrumentOption[]> {
    return this.http
      .get<QuoteMonitorInstrumentOptionsResponseDto>(`${this.baseUrl}/options`)
      .pipe(map(response => this.sortOptions(response.options ?? [])));
  }

  getSelectedInstrumentCodes(): Observable<string[]> {
    return this.http
      .get<QuoteMonitorInstrumentSelectionResponseDto>(this.baseUrl)
      .pipe(map(response => (response.instruments ?? []).map(instrument => instrument.instrumentCode)));
  }

  replaceSelectedInstrumentCodes(instrumentCodes: string[]): Observable<void> {
    const dto: ReplaceQuoteMonitorInstrumentSelectionDto = {
      instrumentCodes: [...instrumentCodes].sort((left, right) => left.localeCompare(right))
    };

    return this.http
      .put<void>(this.baseUrl, dto)
      .pipe(map(() => undefined));
  }

  private sortOptions(options: QuoteMonitorInstrumentOption[]): QuoteMonitorInstrumentOption[] {
    return [...options].sort((left, right) =>
      left.instrumentCode.localeCompare(right.instrumentCode)
    );
  }
}
