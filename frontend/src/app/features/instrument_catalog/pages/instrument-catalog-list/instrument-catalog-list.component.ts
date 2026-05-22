import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';

import { InstrumentCatalogItem } from '../../models/instrument-catalog.model';
import { InstrumentCatalogService } from '../../services/instrument-catalog.service';

@Component({
  selector: 'app-instrument-catalog-list',
  standalone: true,
  imports: [
    CommonModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatSnackBarModule,
    MatTableModule
  ],
  templateUrl: './instrument-catalog-list.component.html',
  styleUrl: './instrument-catalog-list.component.scss'
})
export class InstrumentCatalogListComponent implements OnInit {
  readonly displayed: string[] = [
    'displayName',
    'instrumentCode',
    'asset',
    'product',
    'description',
    'attributes'
  ];

  dataSource = new MatTableDataSource<InstrumentCatalogItem>([]);
  loading = false;

  constructor(
    private readonly service: InstrumentCatalogService,
    private readonly snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    if (this.loading) {
      return;
    }

    this.loading = true;

    this.service.list().subscribe({
      next: instruments => {
        this.dataSource.data = instruments;
        this.loading = false;
      },
      error: err => {
        this.loading = false;
        this.snack.open(this.resolveErrorMessage(err, 'Load instrument catalog failed'), 'Close');
      }
    });
  }

  trackByInstrumentCode(_index: number, instrument: InstrumentCatalogItem): string {
    return instrument.instrumentCode;
  }

  private resolveErrorMessage(err: unknown, fallback: string): string {
    if (!err || typeof err !== 'object') {
      return fallback;
    }

    const body = (err as { error?: unknown }).error;
    if (body && typeof body === 'object') {
      const detail = (body as { detail?: unknown }).detail;
      if (typeof detail === 'string' && detail.trim().length > 0) {
        return detail;
      }

      const title = (body as { title?: unknown }).title;
      if (typeof title === 'string' && title.trim().length > 0) {
        return title;
      }
    }

    const message = (err as { message?: unknown }).message;
    if (typeof message === 'string' && message.trim().length > 0) {
      return message;
    }

    return fallback;
  }
}
