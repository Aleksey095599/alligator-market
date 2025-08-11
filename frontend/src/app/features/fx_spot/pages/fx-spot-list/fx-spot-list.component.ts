import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { FxSpotService } from '../../services/fx-spot.service';
import { FxSpotDto } from '../../models/fx-spot.model';

@Component({
  selector: 'app-fx-spot-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule],
  templateUrl: './fx-spot-list.component.html',
  styleUrl: './fx-spot-list.component.scss'
})
export class FxSpotListComponent implements OnInit {

  /* список колонок таблицы */
  displayed: string[] = ['internalCode', 'pairCode', 'valueDateCode'];
  dataSource = new MatTableDataSource<FxSpotDto>([]);

  constructor(private readonly service: FxSpotService) {}

  /* загрузка списка при открытии страницы */
  ngOnInit(): void {
    this.service.list().subscribe({
      next: list => this.dataSource.data = list,
      error: err => console.error(err)
    });
  }
}
