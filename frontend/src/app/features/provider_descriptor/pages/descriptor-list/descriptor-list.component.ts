import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { ProviderDescriptorService } from '../../services/provider-descriptor.service';
import { ProviderDto } from '../../models/provider-dto.model';

@Component({
  selector: 'app-provider-descriptor-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule],
  templateUrl: './descriptor-list.component.html',
  styleUrls: ['./descriptor-list.component.scss']
})
export class DescriptorListComponent implements OnInit {

  /* Список колонок таблицы (код провайдера намеренно скрыт на уровне API — используем только дружелюбное имя). */
  displayed: string[] = [
    'displayName',
    'deliveryMode',
    'accessMethod',
    'bulkSubscription',
    'minUpdateIntervalSeconds'
  ];
  /* Источник данных для таблицы. */
  dataSource = new MatTableDataSource<ProviderDto>([]);

  constructor(private readonly service: ProviderDescriptorService) {}

  /* Загрузка списка при открытии страницы. */
  ngOnInit(): void {
    this.refresh();
  }

  /* Получить список дескрипторов и обновить таблицу. */
  private refresh(): void {
    this.service.list().subscribe({
      next: list => this.dataSource.data = list,
      error: err => console.error(err)
    });
  }
}
