import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';

import { CreateCurrencyDto, CurrencyResponseDto } from '../../models/currency.model';
import { UpdateCurrencyDto } from '../../models/currency-update.model';
import { CurrencyService } from '../../services/currency.service';

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

  //=================
  // Табличные данные
  //=================
  displayed: string[] = ['code', 'name', 'country', 'fractionDigits', 'actions'];
  dataSource = new MatTableDataSource<CurrencyResponseDto>([]);

  //========================
  // Форма добавления валюты
  //========================
  form: FormGroup;

  locked = false; // флаг блокировки кнопки Add
  editing = false; // режим редактирования
  editCode: string | null = null; // код валюты для редактирования

  constructor(
    private readonly service: CurrencyService,
    private readonly fb: FormBuilder,
    private readonly snack: MatSnackBar
  ) {
    this.form = this.fb.group({
      code: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[A-Z]{3}$/)
        ]
      ],

      name: [
        '',
        [
          Validators.required,
          Validators.maxLength(50)
        ]
      ],

      country: [
        '',
        [
          Validators.required,
          Validators.maxLength(100)
        ]
      ],

      fractionDigits: [
        2,
        [
          Validators.required,
          Validators.min(0),
          Validators.max(10)
        ]
      ]
    });
  }

  //=======================
  // Активность на странице
  //=======================

  /* загрузка списка при открытии страницы */
  ngOnInit(): void {
    this.refresh();
  }

  /* нажата кнопка Add */
  onAdd(): void {
    if (this.form.invalid || this.locked) {
      return;
    }

    this.locked = true; // блокируем кнопку

    const dto: CreateCurrencyDto = this.form.value;

    this.service.add(dto).subscribe({
      next: code => {
        // Если все ОК
        // уведомление
        this.snack.open(
          `Currency '${code}' added`,
          'OK',
          { duration: 2500 }
        );
        this.refresh(); // обновляем таблицу
        this.form.reset({ fractionDigits: 2 }); // оставляем количество знаков по-умолчанию
        this.locked = false; // разблокируем кнопку
      },
      error: err => {
        // Если ошибка
        const msg = err.error?.message ?? err.message ?? 'Add failed'; // ловим сообщение ошибки сервера
        const ref = this.snack.open(msg, 'Close', { duration: 0 }); // уведомление с ошибкой
        ref.afterDismissed().subscribe(() => {
          this.locked = false;
        }); // Close для разблокировки Add
      }
    });
  }

  /* клик по иконке Edit */
  onEdit(c: CurrencyResponseDto): void {
    this.editing = true;
    this.editCode = c.code;
    this.form.setValue({
      code: c.code,
      name: c.name,
      country: c.country,
      fractionDigits: c.fractionDigits
    });
    this.form.controls['code'].disable();
  }

  /* нажата кнопка Save */
  onSave(): void {
    if (this.form.invalid || this.locked || !this.editCode) {
      return;
    }

    this.locked = true;

    const dto: UpdateCurrencyDto = {
      name: this.form.controls['name'].value,
      country: this.form.controls['country'].value,
      fractionDigits: this.form.controls['fractionDigits'].value
    } as UpdateCurrencyDto;

    this.service.update(this.editCode, dto).subscribe({
      next: () => {
        this.snack.open(`Currency '${this.editCode}' updated`, 'OK', { duration: 2500 });
        this.refresh();
        this.cancelEdit();
        this.locked = false;
      },
      error: err => {
        const msg = err.error?.message ?? err.message ?? 'Update failed';
        const ref = this.snack.open(msg, 'Close', { duration: 0 });
        ref.afterDismissed().subscribe(() => {
          this.locked = false;
        });
      }
    });
  }

  /* нажата кнопка Cancel */
  cancelEdit(): void {
    this.editing = false;
    this.editCode = null;
    this.form.reset({ code: '', name: '', country: '', fractionDigits: 2 });
    this.form.controls['code'].enable();
    this.locked = false;
  }

  /* клик по иконке Delete */
  onDelete(code: string): void {
    this.service.delete(code).subscribe({
      next: () => {
        this.snack.open(`Currency '${code}' deleted`, 'OK', { duration: 2500 });
        this.refresh();
      },
      error: err => {
        this.snack.open(err.error?.message ?? err.message ?? 'Delete failed', 'Close');
      }
    });
  }

  //========================
  // Вспомогательные утилиты
  //========================
  /* утилита: перезагрузить список */
  private refresh(): void {
    this.service.list().subscribe({
      next: list => {
        this.dataSource.data = list;
      },
      error: err => {
        this.snack.open(err.message ?? 'Load failed', 'Close');
      }
    });
  }

}
