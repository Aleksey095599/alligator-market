import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatCardModule } from '@angular/material/card';
import { FxOutrightService } from '../../services/fx-outright.service';
import { FxOutrightDto } from '../../models/fx-outright.model';

@Component({
  selector: 'app-fx-outright-list',
  standalone: true,
  imports: [CommonModule, MatTableModule, MatCardModule],
  templateUrl: './fx-outright-list.component.html',
  styleUrl: './fx-outright-list.component.scss'
})
export class FxOutrightListComponent implements OnInit {

  /* список колонок таблицы */
  displayed: string[] = ['internalCode', 'pairCode', 'valueDateCode'];
  dataSource = new MatTableDataSource<FxOutrightDto>([]);

  constructor(private readonly service: FxOutrightService) {}

  /* загрузка списка при открытии страницы */
  ngOnInit(): void {
    this.service.list().subscribe({
      next: list => this.dataSource.data = list,
      error: err => console.error(err)
    });
  }
}
