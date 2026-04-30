import { Injectable } from '@angular/core';
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

/* Лёгкий контракт ошибки backend'а без response-конверта. */
interface ApiErrorPayload {
  errorCode?: unknown;
  message?: unknown;
}

/* Перехватчик ошибок API без зависимости от response-конверта. */
@Injectable()
export class ApiEnvelopeInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        const body = error.error as ApiErrorPayload | undefined;

        if (body && typeof body.errorCode === 'string') {
          // Здесь можно централизованно обрабатывать известные errorCode.
        }

        return throwError(() => error);
      })
    );
  }
}
