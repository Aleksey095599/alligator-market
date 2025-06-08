import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {CurrencyQuoteDto} from '../models/quote.model';

/* Сервис подписки на поток котировок через SSE */
@Injectable({ providedIn: 'root' })
export class QuoteService {

  /* Подписаться на поток SSE */
  stream(): Observable<CurrencyQuoteDto> {
    return new Observable<CurrencyQuoteDto>(subscriber => {
      const es = new EventSource('/api/v1/quotes/stream');
      es.onmessage = ev => subscriber.next(JSON.parse(ev.data));
      es.onerror = err => {
        subscriber.error(err);
        es.close();
      };
      return () => es.close();
    });
  }
}
