import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SourceHandlerPassportDto } from '../models/source-handler-passport-dto.model';

@Injectable({
  providedIn: 'root'
})
export class SourceHandlerPassportService {

  constructor(private http: HttpClient) {}

  private readonly baseUrl = '/api/v1/source-handler-passports';

  list(): Observable<SourceHandlerPassportDto[]> {
    return this.http.get<SourceHandlerPassportDto[]>(this.baseUrl);
  }
}
