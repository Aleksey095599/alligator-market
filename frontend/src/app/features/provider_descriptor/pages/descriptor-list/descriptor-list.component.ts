import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { ProviderDescriptorService } from '../../services/provider-descriptor.service';
import { DescriptorDto } from '../../models/descriptor-dto.model';

@Component({
  selector: 'app-provider-descriptor-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule],
  templateUrl: './descriptor-list.component.html',
  styleUrls: ['./descriptor-list.component.scss']
})
export class DescriptorListComponent implements OnInit {

  /* Список колонок таблицы. */
  displayed: string[] = [
    'displayName',
    'deliveryMode',
    'accessMethod',
    'bulkSubscription'
  ];
  /* Источник данных для таблицы. */
  dataSource = new MatTableDataSource<DescriptorDto>([]);

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
