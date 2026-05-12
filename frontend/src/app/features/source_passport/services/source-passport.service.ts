import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SourcePassportDto } from '../models/source-passport-dto.model';

@Injectable({
  providedIn: 'root'
})
export class SourcePassportService {

  constructor(private http: HttpClient) {}

  private readonly baseUrl = '/api/v1/sources';

  list(): Observable<SourcePassportDto[]> {
    return this.http.get<SourcePassportDto[]>(this.baseUrl);
  }
}
