import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatTooltipModule } from '@angular/material/tooltip';
import { FxSpotListItemDto } from '../../models/fx-spot.model';
import { FxSpotCreateDto } from '../../models/fx-spot-create.model';
import { FxSpotService } from '../../services/fx-spot.service';
import { FxSpotUpdateDto } from '../../models/fx-spot-update.model';
import { CurrencyService } from '../../../currency/services/currency.service';
import { CurrencyResponseDto } from '../../../currency/models/currency.model';
import { FxSpotTenor } from '../../models/fx-spot-tenor.model';

@Component({
  selector: 'app-fx-spot-admin',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatIconModule,
    MatButtonModule,
    MatSnackBarModule,
    MatCardModule,
    MatTooltipModule
  ],
  templateUrl: './fx-spot-admin.component.html',
  styleUrl: './fx-spot-admin.component.scss'
})
export class FxSpotAdminComponent implements OnInit {

  //=================
  // Табличные данные
  //=================
  displayed: string[] = ['instrumentCode', 'symbol', 'tenor', 'defaultQuoteFractionDigits', 'actions'];
  dataSource = new MatTableDataSource<FxSpotListItemDto>([]);

  //=========================================
  // Форма добавления инструмента FX_SPOT
  //=========================================
  form: FormGroup;

  locked = false; // флаг блокировки кнопки Add
  editing = false; // режим редактирования
  editCode: string | null = null; // внутренний код инструмента для редактирования (получаем с backend)
  editSymbol: string | null = null; // символ инструмента для уведомления
  currencies: CurrencyResponseDto[] = [];
  tenors = Object.values(FxSpotTenor);

  constructor(
    private readonly service: FxSpotService,
    private readonly fb: FormBuilder,
    private readonly snack: MatSnackBar,
    private readonly currencyService: CurrencyService
  ) {
    this.form = this.fb.group({
      baseCurrency: [
        '',
        [Validators.required]
      ],

      quoteCurrency: [
        '',
        [Validators.required]
      ],

      defaultQuoteFractionDigits: [
        4,
        [
          Validators.required,
          Validators.min(0),
          Validators.max(10)
        ]
      ],

      tenor: [
        FxSpotTenor.TOD,
        [Validators.required]
      ]
    });
  }

  //=======================
  // Активность на странице
  //=======================

  /* загрузка списка при открытии страницы */
  ngOnInit(): void {
    this.refresh();
    this.currencyService.list().subscribe({
      next: c => {
        this.currencies = c;
      },
      error: err => {
        this.snack.open(err.message ?? 'Load currencies failed', 'Close');
      }
    });
  }

  /* нажата кнопка Add */
  onAdd(): void {
    if (this.form.invalid || this.locked) {
      return;
    }

    this.locked = true; // блокируем кнопку

    const dto = this.form.getRawValue() as FxSpotCreateDto;

    this.service.add(dto).subscribe({
      next: code => {
        // Если все ОК
        this.snack.open(
          `FX Spot '${code}' added`,
          'OK',
          { duration: 2500 }
        );
        this.refresh();
        this.form.reset({
          baseCurrency: '',
          quoteCurrency: '',
          defaultQuoteFractionDigits: 4,
          tenor: FxSpotTenor.TOD
        });
        this.locked = false;
      },
      error: err => {
        const msg = this.resolveErrorMessage(err, 'Add failed');
        const ref = this.snack.open(msg, 'Close', { duration: 0 });
        ref.afterDismissed().subscribe(() => {
          this.locked = false;
        });
      }
    });
  }

  /* клик по иконке Edit */
  onEdit(spot: FxSpotListItemDto): void {
    this.editing = true;
    this.editCode = spot.instrumentCode;
    this.editSymbol = spot.symbol;
    this.form.setValue({
      baseCurrency: spot.baseCurrency,
      quoteCurrency: spot.quoteCurrency,
      defaultQuoteFractionDigits: spot.defaultQuoteFractionDigits,
      tenor: spot.tenor
    });
    this.form.controls['baseCurrency'].disable();
    this.form.controls['quoteCurrency'].disable();
    this.form.controls['tenor'].disable();
  }

  /* нажата кнопка Save */
  onSave(): void {
    if (this.form.invalid || this.locked || !this.editCode) {
      return;
    }

    this.locked = true;

    const dto: FxSpotUpdateDto = {
      defaultQuoteFractionDigits: this.form.controls['defaultQuoteFractionDigits'].value
    } as FxSpotUpdateDto;

    this.service.update(this.editCode, dto).subscribe({
      next: () => {
        const title = this.editSymbol ?? this.editCode;
        this.snack.open(`FX Spot '${title}' updated`, 'OK', { duration: 2500 });
        this.refresh();
        this.cancelEdit();
        this.locked = false;
      },
      error: err => {
        const msg = this.resolveErrorMessage(err, 'Update failed');
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
    this.editSymbol = null;
    this.form.reset({
      baseCurrency: '',
      quoteCurrency: '',
      defaultQuoteFractionDigits: 4,
      tenor: FxSpotTenor.TOD
    });
    this.form.controls['baseCurrency'].enable();
    this.form.controls['quoteCurrency'].enable();
    this.form.controls['tenor'].enable();
    this.locked = false;
  }

  /* клик по иконке Delete */
  onDelete(spot: FxSpotListItemDto): void {
    this.service.delete(spot.instrumentCode).subscribe({
      next: () => {
        this.snack.open(`FX Spot '${spot.symbol}' deleted`, 'OK', { duration: 2500 });
        this.refresh();
      },
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Delete failed'), 'Close');
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
        this.dataSource.data = [...list].sort((left, right) => left.symbol.localeCompare(right.symbol));
      },
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Load failed'), 'Close');
      }
    });
  }

  /* Утилита: извлекаем текст ошибки из стандартного ответа Spring ProblemDetail. */
  private resolveErrorMessage(err: unknown, fallback: string): string {
    if (!err || typeof err !== 'object') {
      return fallback;
    }

    const body = (err as { error?: unknown }).error;
    if (!body || typeof body !== 'object') {
      return (err as { message?: string }).message ?? fallback;
    }

    const detail = (body as { detail?: unknown }).detail;
    if (typeof detail === 'string' && detail.trim().length > 0) {
      return detail;
    }

    const title = (body as { title?: unknown }).title;
    if (typeof title === 'string' && title.trim().length > 0) {
      return title;
    }

    return (err as { message?: string }).message ?? fallback;
  }

}
