import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

import { SourceHandlerPassportDto } from '../../models/source-handler-passport-dto.model';
import { SourceHandlerPassportService } from '../../services/source-handler-passport.service';

@Component({
  selector: 'app-source-handler-passport-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule, MatSnackBarModule],
  templateUrl: './source-handler-passport-list.component.html',
  styleUrl: './source-handler-passport-list.component.scss'
})
export class SourceHandlerPassportListComponent implements OnInit {

  displayed: string[] = [
    'sourceCode',
    'handlerCode',
    'deliveryMode',
    'accessMethod',
    'registryStatus'
  ];
  dataSource = new MatTableDataSource<SourceHandlerPassportDto>([]);

  constructor(
    private readonly service: SourceHandlerPassportService,
    private readonly snack: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.refresh();
  }

  private refresh(): void {
    this.service.list().subscribe({
      next: list => {
        this.dataSource.data = [...list].sort((left, right) =>
          this.comparePassports(left, right)
        );
      },
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Load source handler passports failed'), 'Close');
      }
    });
  }

  private comparePassports(
    left: SourceHandlerPassportDto,
    right: SourceHandlerPassportDto
  ): number {
    const sourceOrder = left.sourceCode.localeCompare(
      right.sourceCode,
      'en',
      { sensitivity: 'base' }
    );
    if (sourceOrder !== 0) {
      return sourceOrder;
    }

    return left.handlerCode.localeCompare(
      right.handlerCode,
      'en',
      { sensitivity: 'base' }
    );
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
