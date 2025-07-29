import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { ProviderProfileService } from '../../services/provider-profile.service';
import { ProviderProfileDto } from '../../models/provider-profile.model';

@Component({
  selector: 'app-provider-profile-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule],
  templateUrl: './profile-list.component.html',
  styleUrl: './profile-list.component.scss'
})
export class ProfileListComponent implements OnInit {

  displayed: string[] = [
    'providerCode',
    'displayName',
    'instrumentTypes',
    'deliveryMode',
    'accessMethod',
    'supportsBulkSubscription',
    'minPollPeriodMs'
  ];
  dataSource = new MatTableDataSource<ProviderProfileDto>([]);

  constructor(private readonly service: ProviderProfileService) {}

  /* загрузка списка при открытии страницы */
  ngOnInit(): void {
    this.service.list().subscribe({
      next: list => this.dataSource.data = list,
      error: err => console.error(err)
    });
  }
}
