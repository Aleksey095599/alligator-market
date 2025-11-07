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
import { ApiResponse } from '../api/api-response.model';

/* Перехватчик для конверта ответов API. */
@Injectable()
export class ApiEnvelopeInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        const body = error.error as ApiResponse<unknown> | undefined;

        if (body && body.success === false && body.errorCode) {
          // Здесь можно централизованно обрабатывать известные errorCode.
        }

        return throwError(() => error);
      })
    );
  }
}
