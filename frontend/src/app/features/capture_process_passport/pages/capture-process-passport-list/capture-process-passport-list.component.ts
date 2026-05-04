import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { CaptureProcessPassportDto } from '../../models/capture-process-passport-dto.model';
import { CaptureProcessPassportService } from '../../services/capture-process-passport.service';

@Component({
  selector: 'app-capture-process-passport-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule, MatSnackBarModule],
  templateUrl: './capture-process-passport-list.component.html',
  styleUrl: './capture-process-passport-list.component.scss'
})
export class CaptureProcessPassportListComponent implements OnInit {

  /* Список колонок таблицы. */
  displayed: string[] = [
    'captureProcessCode',
    'displayName'
  ];
  /* Источник данных для таблицы. */
  dataSource = new MatTableDataSource<CaptureProcessPassportDto>([]);

  constructor(
    private readonly service: CaptureProcessPassportService,
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
          left.captureProcessCode.localeCompare(right.captureProcessCode, 'en', { sensitivity: 'base' })
        );
      },
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Load capture process passports failed'), 'Close');
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
