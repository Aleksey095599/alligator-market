import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

import {
  LiveQuoteUpdate,
  QuoteMonitorLiveQuoteDto,
  QuoteMonitorLiveQuoteListResponseDto
} from '../models/quote-monitor-live-quote.model';

@Injectable({
  providedIn: 'root'
})
export class QuoteMonitorLiveQuoteService {
  private readonly baseUrl = '/api/v1/quote-monitor/live-quotes';

  constructor(private readonly http: HttpClient) {}

  getCurrentQuotes(): Observable<LiveQuoteUpdate[]> {
    return this.http
      .get<QuoteMonitorLiveQuoteListResponseDto>(this.baseUrl)
      .pipe(map(response => (response.quotes ?? []).map(quote => this.toLiveQuoteUpdate(quote))));
  }

  private toLiveQuoteUpdate(quote: QuoteMonitorLiveQuoteDto): LiveQuoteUpdate {
    return {
      instrumentCode: quote.instrumentCode,
      lastPrice: quote.lastPrice,
      sourceCode: quote.sourceCode,
      sourceTimestamp: quote.sourceTimestamp,
      receivedAt: quote.receivedAt
    };
  }
}
