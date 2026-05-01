import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { ProviderPassportService } from '../../services/provider-passport.service';
import { ProviderDto } from '../../models/provider-dto.model';

@Component({
  selector: 'app-provider-passport-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule, MatSnackBarModule],
  templateUrl: './passport-list.component.html',
  styleUrl: './passport-list.component.scss'
})
export class PassportListComponent implements OnInit {

  /* Список колонок таблицы. */
  displayed: string[] = [
    'providerCode',
    'displayName',
    'deliveryMode',
    'accessMethod',
    'bulkSubscription'
  ];
  /* Источник данных для таблицы. */
  dataSource = new MatTableDataSource<ProviderDto>([]);

  constructor(
    private readonly service: ProviderPassportService,
    private readonly snack: MatSnackBar
  ) {}

  /* Загрузка списка при открытии страницы. */
  ngOnInit(): void {
    this.refresh();
  }

  /* Получить список паспортов и обновить таблицу. */
  private refresh(): void {
    this.service.list().subscribe({
      next: list => {
        this.dataSource.data = [...list].sort((left, right) =>
          left.providerCode.localeCompare(right.providerCode, 'en', { sensitivity: 'base' })
        );
      },
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Load provider passports failed'), 'Close');
      }
    });
  }

  /* Извлекаем текст ошибки из стандартного HttpErrorResponse/ProblemDetail. */
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
