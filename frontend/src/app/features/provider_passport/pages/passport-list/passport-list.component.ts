import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { ProviderPassportService } from '../../services/provider-passport.service';
import { ProviderDto } from '../../models/provider-dto.model';

@Component({
  selector: 'app-provider-passport-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule],
  templateUrl: './passport-list.component.html',
  styleUrl: './passport-list.component.scss'
})
export class PassportListComponent implements OnInit {

  /* Список колонок таблицы. */
  displayed: string[] = [
    'displayName',
    'deliveryMode',
    'accessMethod',
    'bulkSubscription'
  ];
  /* Источник данных для таблицы. */
  dataSource = new MatTableDataSource<ProviderDto>([]);

  constructor(private readonly service: ProviderPassportService) {}

  /* Загрузка списка при открытии страницы. */
  ngOnInit(): void {
    this.refresh();
  }

  /* Получить список паспортов и обновить таблицу. */
  private refresh(): void {
    this.service.list().subscribe({
      next: list => {
        this.dataSource.data = list;
      },
      error: err => {
        console.error(err);
      }
    });
  }
}
