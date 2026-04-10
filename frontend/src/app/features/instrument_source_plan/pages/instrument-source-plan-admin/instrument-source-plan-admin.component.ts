import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';

import {
  InstrumentSourcePlanResponseDto,
  MarketDataSourceResponseDto
} from '../../models/instrument-source-plan.model';
import {
  InstrumentOptionDto,
  ProviderOptionDto
} from '../../models/instrument-source-plan-options.model';
import { MarketDataSourceRequestDto } from '../../models/create-instrument-source-plan.model';
import { InstrumentSourcePlanService } from '../../services/instrument-source-plan.service';

/* Админ-страница управления whole-plan sourcing планами по инструментам. */
@Component({
  selector: 'app-instrument-source-plan-admin',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatTableModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatCheckboxModule,
    MatIconModule,
    MatButtonModule,
    MatSnackBarModule,
    MatCardModule
  ],
  templateUrl: './instrument-source-plan-admin.component.html',
  styleUrl: './instrument-source-plan-admin.component.scss'
})
export class InstrumentSourcePlanAdminComponent implements OnInit {

  /* Колонки таблицы с существующими планами. */
  displayed: string[] = ['instrumentCode', 'sourceCount', 'firstProvider', 'actions'];
  dataSource = new MatTableDataSource<InstrumentSourcePlanResponseDto>([]);

  /* Опции для селектов формы. */
  instruments: InstrumentOptionDto[] = [];
  providers: ProviderOptionDto[] = [];

  /* Флаг блокировки кнопок при запросе. */
  locked = false;
  /* Режим редактора: create/edit. */
  editing = false;
  /* Текущий выбранный план в режиме редактирования. */
  selectedInstrumentCode: string | null = null;

  /* Главная форма whole-plan редактора. */
  form: FormGroup;

  constructor(
    private readonly service: InstrumentSourcePlanService,
    private readonly fb: FormBuilder,
    private readonly snack: MatSnackBar
  ) {
    this.form = this.fb.group({
      instrumentCode: ['', [Validators.required]],
      sources: this.fb.array([])
    });
  }

  /* Загрузка данных при открытии страницы. */
  ngOnInit(): void {
    this.refreshList();
    this.loadOptions();
    this.onAddSourceRow();
  }

  /* Удобный доступ к FormArray sources. */
  get sources(): FormArray {
    return this.form.controls['sources'] as FormArray;
  }

  /* Быстрый список source-контролов для шаблона. */
  get sourceControls(): AbstractControl[] {
    return this.sources.controls;
  }

  /* Есть ли дубликаты провайдеров в редакторе. */
  get hasDuplicateProviders(): boolean {
    const values = this.sourceControls
      .map(c => c.get('providerCode')?.value)
      .filter(v => !!v);

    return new Set(values).size !== values.length;
  }

  /* Есть ли дубликаты priority в редакторе. */
  get hasDuplicatePriorities(): boolean {
    const values = this.sourceControls
      .map(c => c.get('priority')?.value)
      .filter(v => v !== null && v !== undefined && v !== '');

    return new Set(values).size !== values.length;
  }

  /* Признак невалидного состояния, блокирующий сохранение. */
  get hasPlanValidationError(): boolean {
    return this.form.invalid
      || this.sources.length === 0
      || this.hasDuplicateProviders
      || this.hasDuplicatePriorities;
  }

  /* Перезагрузить список существующих plans. */
  refreshList(): void {
    this.service.list().subscribe({
      next: plans => {
        this.dataSource.data = plans;
      },
      error: () => {
        this.snack.open('Load source plans failed', 'Close');
      }
    });
  }

  /* Загрузить опции для селектов editor-а. */
  loadOptions(): void {
    this.service.getOptions().subscribe({
      next: options => {
        this.instruments = options.instruments;
        this.providers = options.providers;
      },
      error: () => {
        this.snack.open('Load options failed', 'Close');
      }
    });
  }

  /* Добавить новую строку source в FormArray. */
  onAddSourceRow(source?: MarketDataSourceResponseDto): void {
    this.sources.push(this.fb.group({
      providerCode: [source?.providerCode ?? '', [Validators.required]],
      active: [source?.active ?? true],
      priority: [source?.priority ?? 0, [Validators.required, Validators.min(0)]]
    }));
  }

  /* Удалить строку source по индексу. */
  onRemoveSourceRow(index: number): void {
    if (this.sources.length <= 1) {
      return;
    }

    this.sources.removeAt(index);
  }

  /* Загрузить план в editor и перейти в edit mode. */
  onLoadPlan(plan: InstrumentSourcePlanResponseDto): void {
    this.service.get(plan.instrumentCode).subscribe({
      next: fullPlan => {
        this.editing = true;
        this.selectedInstrumentCode = fullPlan.instrumentCode;

        this.form.controls['instrumentCode'].setValue(fullPlan.instrumentCode);
        this.form.controls['instrumentCode'].disable();

        this.sources.clear();
        fullPlan.sources.forEach(source => this.onAddSourceRow(source));

        if (this.sources.length === 0) {
          this.onAddSourceRow();
        }
      },
      error: err => {
        this.snack.open(err.error?.message ?? err.message ?? 'Load plan failed', 'Close');
      }
    });
  }

  /* Создать новый whole-plan из формы. */
  onCreate(): void {
    if (this.hasPlanValidationError || this.locked) {
      return;
    }

    this.locked = true;

    const instrumentCode = this.form.controls['instrumentCode'].value;
    const sources = this.collectSortedSources();

    this.service.create({ instrumentCode, sources }).subscribe({
      next: () => {
        this.snack.open(`Plan for '${instrumentCode}' created`, 'OK', { duration: 2500 });
        this.refreshList();
        this.onReset();
        this.locked = false;
      },
      error: err => {
        const msg = err.error?.message ?? err.message ?? 'Create failed';
        const ref = this.snack.open(msg, 'Close', { duration: 0 });
        ref.afterDismissed().subscribe(() => {
          this.locked = false;
        });
      }
    });
  }

  /* Сохранить изменения whole-plan в edit mode. */
  onSave(): void {
    if (this.hasPlanValidationError || this.locked || !this.selectedInstrumentCode) {
      return;
    }

    this.locked = true;
    const instrumentCode = this.selectedInstrumentCode;
    const sources = this.collectSortedSources();

    this.service.replace(instrumentCode, { sources }).subscribe({
      next: () => {
        this.snack.open(`Plan for '${instrumentCode}' replaced`, 'OK', { duration: 2500 });
        this.refreshList();
        this.locked = false;
      },
      error: err => {
        const msg = err.error?.message ?? err.message ?? 'Replace failed';
        const ref = this.snack.open(msg, 'Close', { duration: 0 });
        ref.afterDismissed().subscribe(() => {
          this.locked = false;
        });
      }
    });
  }

  /* Удалить выбранный план из editor-а. */
  onDeleteSelected(): void {
    if (!this.selectedInstrumentCode || this.locked) {
      return;
    }

    this.deletePlan(this.selectedInstrumentCode, true);
  }

  /* Удалить план из нижнего списка. */
  onDeleteFromList(instrumentCode: string): void {
    const isCurrentPlan = this.selectedInstrumentCode === instrumentCode;
    this.deletePlan(instrumentCode, isCurrentPlan);
  }

  /* Сбросить форму и вернуться в create mode. */
  onReset(): void {
    this.editing = false;
    this.selectedInstrumentCode = null;
    this.locked = false;

    this.form.reset({ instrumentCode: '' });
    this.form.controls['instrumentCode'].enable();

    this.sources.clear();
    this.onAddSourceRow();
  }

  /* Отменить режим редактирования. */
  cancelEdit(): void {
    this.onReset();
  }

  /* Получить first provider для обзорной таблицы. */
  firstProvider(plan: InstrumentSourcePlanResponseDto): string {
    if (!plan.sources.length) {
      return '—';
    }

    const sorted = [...plan.sources].sort((a, b) => a.priority - b.priority);
    return sorted[0].providerCode;
  }

  /* Привести source-строки к DTO и отсортировать по priority перед отправкой. */
  private collectSortedSources(): MarketDataSourceRequestDto[] {
    return this.sources.getRawValue()
      .map((row: { providerCode: string; active: boolean; priority: number }) => ({
        providerCode: row.providerCode,
        active: row.active,
        priority: Number(row.priority)
      }))
      .sort((a, b) => a.priority - b.priority);
  }

  /* Унифицированное удаление плана (из editor или из списка). */
  private deletePlan(instrumentCode: string, resetEditor: boolean): void {
    this.service.delete(instrumentCode).subscribe({
      next: () => {
        this.snack.open(`Plan for '${instrumentCode}' deleted`, 'OK', { duration: 2500 });
        this.refreshList();

        if (resetEditor) {
          this.onReset();
        }
      },
      error: err => {
        this.snack.open(err.error?.message ?? err.message ?? 'Delete failed', 'Close');
      }
    });
  }
}
