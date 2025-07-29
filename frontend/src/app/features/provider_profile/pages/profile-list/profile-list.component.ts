import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { MatSlideToggleModule, MatSlideToggleChange } from '@angular/material/slide-toggle';
import { ProviderProfileService } from '../../services/provider-profile.service';
import { ProviderProfileStatusDto } from '../../models/provider-profile-status.model';

@Component({
  selector: 'app-provider-profile-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule, MatSlideToggleModule],
  templateUrl: './profile-list.component.html',
  styleUrl: './profile-list.component.scss'
})
export class ProfileListComponent implements OnInit {

  displayed: string[] = [
    'status',
    'providerCode',
    'displayName',
    'instrumentTypes',
    'deliveryMode',
    'accessMethod',
    'supportsBulkSubscription',
    'minPollPeriodMs'
  ];
  dataSource = new MatTableDataSource<ProviderProfileStatusDto>([]);
  showAll = false;

  constructor(private readonly service: ProviderProfileService) {}

  /* загрузка списка при открытии страницы */
  ngOnInit(): void {
    this.refresh();
  }

  /* переключение отображения всех статусов */
  onToggle(event: MatSlideToggleChange): void {
    this.showAll = event.checked;
    this.refresh();
  }

  /* обновление таблицы в зависимости от режима */
  private refresh(): void {
    if (this.showAll) {
      this.service.listAll().subscribe({
        next: list => this.dataSource.data = list,
        error: err => console.error(err)
      });
    } else {
      this.service.list().subscribe({
        next: list => this.dataSource.data = list.map(p => ({
          ...p,
          status: 'ACTIVE'
        } as ProviderProfileStatusDto)),
        error: err => console.error(err)
      });
    }
  }
}
