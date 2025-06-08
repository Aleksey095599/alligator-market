import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MatCardModule} from '@angular/material/card';
import {QuoteService} from '../../services/quote.service';
import {CurrencyQuoteDto} from '../../models/quote.model';

/* Страница отображения котировок */
@Component({
  selector: 'app-quotes',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatCardModule
  ],
  templateUrl: './quotes.component.html',
  styleUrl: './quotes.component.scss'
})
export class QuotesComponent implements OnInit {

  displayed: string[] = ['pairId', 'bid', 'ask', 'ts'];
  dataSource = new MatTableDataSource<CurrencyQuoteDto>([]);

  constructor(private service: QuoteService) {}

  ngOnInit(): void {
    this.service.stream().subscribe({
      next: q => {
        const data = [q, ...this.dataSource.data].slice(0, 50);
        this.dataSource.data = data;
      }
    });
  }
}
