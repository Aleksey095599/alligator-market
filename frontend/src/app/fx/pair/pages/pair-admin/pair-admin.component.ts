import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {MatCardModule} from '@angular/material/card';
import {PairDto} from '../../models/pair.model';
import {PairCreateDto} from '../../models/pair-create.model';
import {PairService} from '../../services/pair.service';
import {PairUpdateDto} from '../../models/pair-update.model';

@Component({
  selector: 'app-pair-admin',
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
  templateUrl: './pair-admin.component.html',
  styleUrl: './pair-admin.component.scss'
})
export class PairAdminComponent implements OnInit {

  //=================
  // Табличные данные
  //=================
  displayed: string[] = ['pair', 'code1', 'code2', 'decimal', 'actions'];
  dataSource  = new MatTableDataSource<PairDto>([]);

  //==============================
  // Форма добавления валютной пары
  //==============================
  form: FormGroup;

  locked = false; // флаг блокировки кнопки Add
  editing = false; // режим редактирования
  editPair: string | null = null; // код пары для редактирования

  constructor(
    private readonly service: PairService,
    private readonly fb: FormBuilder,
    private readonly snack: MatSnackBar
  ) {
    this.form = this.fb.group({
      code1: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[A-Z]{3}$/)
        ]
      ],

      code2: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[A-Z]{3}$/)
        ]
      ],

      decimal: [
        4,
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
    if (this.form.invalid || this.locked) { return; }

    this.locked = true; // блокируем кнопку

    const dto: PairCreateDto = this.form.value;

    this.service.add(dto).subscribe({
      next: pair => {
        // Если все ОК
        this.snack.open(
          `Pair '${pair}' added`, 'OK', { duration: 2500 }
        );
        this.refresh();
        this.form.reset({ decimal: 2 });
        this.locked = false;
      },
      error: err => {
        const msg = err.error?.message ?? err.message ?? 'Add failed';
        const ref =
          this.snack.open(msg, 'Close', { duration: 0 });
        ref.afterDismissed().subscribe(() => this.locked = false);
      }
    });
  }

  /* клик по иконке Edit */
  onEdit(p: PairDto): void {
    this.editing = true;
    this.editPair = p.pair;
    this.form.setValue({
      code1: p.code1,
      code2: p.code2,
      decimal: p.decimal
    });
    this.form.controls['code1'].disable();
    this.form.controls['code2'].disable();
  }

  /* нажата кнопка Save */
  onSave(): void {
    if (this.form.invalid || this.locked || !this.editPair) { return; }

    this.locked = true;

    const dto: PairUpdateDto = {
      decimal: this.form.controls['decimal'].value
    } as PairUpdateDto;

    this.service.update(this.editPair, dto).subscribe({
      next: () => {
        this.snack.open(`Pair '${this.editPair}' updated`, 'OK', { duration: 2500 });
        this.refresh();
        this.cancelEdit();
        this.locked = false;
      },
      error: err => {
        const msg = err.error?.message ?? err.message ?? 'Update failed';
        const ref = this.snack.open(msg, 'Close', { duration: 0 });
        ref.afterDismissed().subscribe(() => this.locked = false);
      }
    });
  }

  /* нажата кнопка Cancel */
  cancelEdit(): void {
    this.editing = false;
    this.editPair = null;
    this.form.reset({ code1: '', code2: '', decimal: 2 });
    this.form.controls['code1'].enable();
    this.form.controls['code2'].enable();
    this.locked = false;
  }

  /* клик по иконке Delete */
  onDelete(pair: string): void {
    this.service.delete(pair).subscribe({
      next: () => {
        this.snack.open(`Pair '${pair}' deleted`, 'OK', { duration: 2500 });
        this.refresh();
      },
      error: err =>
        this.snack.open(err.error?.message ?? err.message ?? 'Delete failed', 'Close')
    });
  }

  //========================
  // Вспомогательные утилиты
  //========================
  /* утилита: перезагрузить список */
  private refresh(): void {
    this.service.list().subscribe({
      next: list => this.dataSource.data = list,
      error: err => this.snack.open(err.message ?? 'Load failed', 'Close')
    });
  }

}
