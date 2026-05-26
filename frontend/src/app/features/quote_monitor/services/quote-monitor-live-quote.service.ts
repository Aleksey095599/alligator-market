import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import {
  QuoteMonitorInstrumentQuote,
  QuoteMonitorInstrumentQuoteDto,
  QuoteMonitorInstrumentQuoteListResponseDto
} from '../models/quote-monitor-live-quote.model';

@Injectable({
  providedIn: 'root'
})
export class QuoteMonitorLiveQuoteService {
  private readonly baseUrl = '/api/v1/quote-monitor/live-quotes';

  constructor(private readonly http: HttpClient) {}

  getSnapshot(): Observable<QuoteMonitorInstrumentQuote[]> {
    return this.http
      .get<QuoteMonitorInstrumentQuoteListResponseDto>(`${this.baseUrl}/snapshot`)
      .pipe(map(response => (response.quotes ?? []).map(quote => this.toInstrumentQuote(quote))));
  }

  openStream(): EventSource {
    return new EventSource(`${this.baseUrl}/stream`);
  }

  parseStreamMessage(data: string): QuoteMonitorInstrumentQuote {
    return this.toInstrumentQuote(JSON.parse(data) as QuoteMonitorInstrumentQuoteDto);
  }

  private toInstrumentQuote(quote: QuoteMonitorInstrumentQuoteDto): QuoteMonitorInstrumentQuote {
    return {
      instrumentCode: quote.instrumentCode,
      lastPrice: quote.lastPrice,
      sourceCode: quote.sourceCode,
      sourceTickTime: quote.sourceTickTime,
      receivedAt: quote.receivedAt
    };
  }
}
