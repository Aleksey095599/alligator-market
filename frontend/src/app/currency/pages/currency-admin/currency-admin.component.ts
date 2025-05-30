import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {CurrencyDto} from '../../models/currency.model';
import {CurrencyService} from '../../services/currency.service';
import {MatCardModule} from "@angular/material/card";

@Component({
  selector: 'app-currency-admin',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatSnackBarModule,
    MatCardModule
  ],
  templateUrl: './currency-admin.component.html',
  styleUrl: './currency-admin.component.scss'
})
export class CurrencyAdminComponent implements OnInit {

  /* ---------------- табличные данные ---------------- */
  displayed: string[] = ['code', 'name', 'country', 'decimal', 'actions'];
  dataSource  = new MatTableDataSource<CurrencyDto>([]);

  /* ---------------- форма добавления ---------------- */
  form: FormGroup;

  constructor(
    private readonly service: CurrencyService,
    private readonly fb: FormBuilder,
    private readonly snack: MatSnackBar
  ) {
    this.form = this.fb.group({
      code: ['', [Validators.required, Validators.maxLength(3)]],
      name: ['', Validators.required],
      country: ['', Validators.required],
      decimal: [2, [Validators.required, Validators.min(0), Validators.max(8)]]
    });
  }

  /* загрузка списка при открытии страницы */
  ngOnInit(): void {
    this.refresh();
  }

  /* нажата кнопка Add */
  onAdd(): void {
    if (this.form.invalid) { return; }

    const dto: CurrencyDto = this.form.value;
    this.service.add(dto).subscribe({
      next: code => {
        this.snack.open(`Currency ${code} added`, 'OK', { duration: 2500 });
        this.refresh();
        this.form.reset({ decimal: 2 });   // оставляем decimal по-умолчанию
      },
      error: err => this.snack.open(err.message ?? 'Add failed', 'Close')
    });
  }

  /* клик по иконке Delete */
  onDelete(code: string): void {
    this.service.delete(code).subscribe({
      next: () => {
        this.snack.open(`Currency ${code} deleted`, 'OK', { duration: 2500 });
        this.refresh();
      },
      error: err => this.snack.open(err.message ?? 'Delete failed', 'Close')
    });
  }

  /* утилита: перезагрузить список */
  private refresh(): void {
    this.service.list().subscribe({
      next: list => this.dataSource.data = list,
      error: err => this.snack.open(err.message ?? 'Load failed', 'Close')
    });
  }

}
