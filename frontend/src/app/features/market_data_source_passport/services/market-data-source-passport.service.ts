import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { MarketDataSourcePassportDto } from '../models/market-data-source-passport-dto.model';

@Injectable({
  providedIn: 'root'
})
export class MarketDataSourcePassportService {

  constructor(private http: HttpClient) {}

  private readonly baseUrl = '/api/v1/market-data-sources';

  list(): Observable<MarketDataSourcePassportDto[]> {
    return this.http.get<MarketDataSourcePassportDto[]>(this.baseUrl);
  }
}
