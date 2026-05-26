import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import {
  QuoteMonitorInstrumentOption,
  QuoteMonitorInstrumentOptionsResponseDto,
  QuoteMonitorSelectedInstrument,
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
    return this.getSelectedInstruments()
      .pipe(map(instruments => instruments.map(instrument => instrument.instrumentCode)));
  }

  getSelectedInstruments(): Observable<QuoteMonitorSelectedInstrument[]> {
    return this.http
      .get<QuoteMonitorInstrumentSelectionResponseDto>(this.baseUrl)
      .pipe(map(response => this.sortSelectedInstruments(response.instruments ?? [])));
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

  private sortSelectedInstruments(
    instruments: QuoteMonitorSelectedInstrument[]
  ): QuoteMonitorSelectedInstrument[] {
    return [...instruments].sort((left, right) =>
      left.instrumentCode.localeCompare(right.instrumentCode)
    );
  }
}
