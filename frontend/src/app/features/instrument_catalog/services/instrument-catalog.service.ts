import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import {
  InstrumentCatalogItem,
  InstrumentCatalogItemDto
} from '../models/instrument-catalog.model';

@Injectable({
  providedIn: 'root'
})
export class InstrumentCatalogService {
  private readonly baseUrl = '/api/v1/instruments/catalog';

  constructor(private readonly http: HttpClient) {}

  list(): Observable<InstrumentCatalogItem[]> {
    return this.http
      .get<InstrumentCatalogItemDto[]>(this.baseUrl)
      .pipe(map(response => this.sortCatalog((response ?? []).map(item => this.toModel(item)))));
  }

  private toModel(dto: InstrumentCatalogItemDto): InstrumentCatalogItem {
    return {
      instrumentCode: dto.instrumentCode,
      displayName: dto.displayName || dto.instrumentCode,
      asset: dto.asset,
      product: dto.product,
      description: dto.description,
      attributes: [...(dto.attributes ?? [])]
    };
  }

  private sortCatalog(items: InstrumentCatalogItem[]): InstrumentCatalogItem[] {
    return [...items].sort((left, right) => {
      const displayNameCompare = left.displayName.localeCompare(right.displayName);

      if (displayNameCompare !== 0) {
        return displayNameCompare;
      }

      return left.instrumentCode.localeCompare(right.instrumentCode);
    });
  }
}
