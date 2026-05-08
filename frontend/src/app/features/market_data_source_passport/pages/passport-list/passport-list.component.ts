import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MarketDataSourcePassportService } from '../../services/market-data-source-passport.service';
import { MarketDataSourcePassportDto } from '../../models/market-data-source-passport-dto.model';

@Component({
  selector: 'app-market-data-source-passport-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule, MatSnackBarModule],
  templateUrl: './passport-list.component.html',
  styleUrl: './passport-list.component.scss'
})
export class PassportListComponent implements OnInit {

  displayed: string[] = [
    'sourceCode',
    'displayName',
    'deliveryMode',
    'accessMethod',
    'bulkSubscription',
    'lifecycleStatus'
  ];
  dataSource = new MatTableDataSource<MarketDataSourcePassportDto>([]);

  constructor(
    private readonly service: MarketDataSourcePassportService,
    private readonly snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.refresh();
  }

  private refresh(): void {
    this.service.list().subscribe({
      next: list => {
        this.dataSource.data = [...list].sort((left, right) =>
          left.sourceCode.localeCompare(right.sourceCode, 'en', { sensitivity: 'base' })
        );
      },
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Load market data source passports failed'), 'Close');
      }
    });
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
