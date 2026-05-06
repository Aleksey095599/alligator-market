import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';

import {
  MarketDataSourcePlanResponseDto,
  MarketDataSourceResponseDto
} from '../../models/market-data-source-plan.model';
import {
  MarketDataCaptureProcessOptionDto,
  InstrumentOptionDto,
  ProviderOptionDto
} from '../../models/market-data-source-plan-options.model';
import { MarketDataSourceRequestDto } from '../../models/create-market-data-source-plan.model';
import { MarketDataSourcePlanService } from '../../services/market-data-source-plan.service';

/* Админ-страница управления whole-plan sourcing планами по инструментам. */
@Component({
  selector: 'app-market-data-source-plan-admin',
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
    MatCardModule
  ],
  templateUrl: './market-data-source-plan-admin.component.html',
  styleUrl: './market-data-source-plan-admin.component.scss'
})
export class MarketDataSourcePlanAdminComponent implements OnInit {

  private static readonly notBlank = (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (typeof value !== 'string') {
      return null;
    }

    return value.trim().length === 0 ? { blank: true } : null;
  };

  /* Колонки таблицы с существующими планами. */
  displayed: string[] = [
    'captureProcessCode',
    'instrumentCode',
    'sourceCount',
    'retiredCount',
    'firstActiveProvider',
    'actions'
  ];
  dataSource = new MatTableDataSource<MarketDataSourcePlanResponseDto>([]);

  /* Опции для option формы. */
  captureProcesses: MarketDataCaptureProcessOptionDto[] = [];
  instruments: InstrumentOptionDto[] = [];
  providers: ProviderOptionDto[] = [];

  /* Флаг блокировки кнопок при запросе. */
  locked = false;
  /* Режим редактора: create/edit. */
  editing = false;
  /* Текущий выбранный план в режиме редактирования. */
  selectedMarketDataCaptureProcessCode: string | null = null;
  selectedInstrumentCode: string | null = null;
  private loadedSourcesFingerprint: string | null = null;

  /* Главная форма whole-plan редактора. */
  form: FormGroup;

  constructor(
    private readonly service: MarketDataSourcePlanService,
    private readonly fb: FormBuilder,
    private readonly snack: MatSnackBar
  ) {
    this.form = this.fb.group({
      captureProcessCode: ['', [Validators.required, MarketDataSourcePlanAdminComponent.notBlank]],
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

  /* Есть ли реальные изменения источников в режиме редактирования. */
  get hasPlanChanges(): boolean {
    if (!this.editing || this.loadedSourcesFingerprint === null) {
      return true;
    }

    return this.sourcesFingerprint(this.collectSortedSources()) !== this.loadedSourcesFingerprint;
  }

  /* Перезагрузить список существующих plans. */
  refreshList(): void {
    this.service.list().subscribe({
      next: plans => {
        this.dataSource.data = plans;
      },
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Load source plans failed'), 'Close');
      }
    });
  }

  /* Загрузить опции для option editor-а. */
  loadOptions(): void {
    this.service.getOptions().subscribe({
      next: options => {
        this.captureProcesses = options.captureProcesses;
        this.instruments = options.instruments;
        this.providers = options.providers;
      },
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Load options failed'), 'Close');
      }
    });
  }

  /* Добавить новую строку source в FormArray. */
  onAddSourceRow(source?: MarketDataSourceResponseDto): void {
    this.sources.push(this.fb.group({
      providerCode: [source?.providerCode ?? '', [Validators.required]],
      priority: [source?.priority ?? 0, [Validators.required, Validators.min(0)]],
      lifecycleStatus: [source?.lifecycleStatus ?? 'ACTIVE']
    }));
  }

  /* Удалить строку source по индексу. */
  onRemoveSourceRow(index: number): void {
    if (this.sources.length <= 1) {
      return;
    }

    this.sources.removeAt(index);
  }

  /* Открыть план в editor и перейти в edit mode. */
  onEditPlan(plan: MarketDataSourcePlanResponseDto): void {
    this.service.get(plan.captureProcessCode, plan.instrumentCode).subscribe({
      next: fullPlan => {
        this.editing = true;
        this.selectedMarketDataCaptureProcessCode = fullPlan.captureProcessCode;
        this.selectedInstrumentCode = fullPlan.instrumentCode;

        this.form.controls['captureProcessCode'].setValue(fullPlan.captureProcessCode);
        this.form.controls['captureProcessCode'].disable();
        this.form.controls['instrumentCode'].setValue(fullPlan.instrumentCode);
        this.form.controls['instrumentCode'].disable();

        this.sources.clear();
        fullPlan.sources.forEach(source => this.onAddSourceRow(source));

        if (this.sources.length === 0) {
          this.onAddSourceRow();
        }

        this.loadedSourcesFingerprint = this.sourcesFingerprint(fullPlan.sources);
      },
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Edit plan failed'), 'Close');
      }
    });
  }

  /* Создать новый whole-plan из формы. */
  onCreate(): void {
    if (this.hasPlanValidationError || this.locked) {
      return;
    }

    this.locked = true;

    const captureProcessCode = this.form.controls['captureProcessCode'].value;
    const instrumentCode = this.form.controls['instrumentCode'].value;
    const sources = this.collectSortedSources();

    this.service.create({ captureProcessCode, instrumentCode, sources }).subscribe({
      next: () => {
        this.snack.open(
          `Plan '${captureProcessCode}' for '${instrumentCode}' created`,
          'OK',
          { duration: 2500 }
        );
        this.refreshList();
        this.onReset();
        this.locked = false;
      },
      error: err => {
        this.unlockAfterError(err, 'Create failed');
      }
    });
  }

  /* Сохранить изменения whole-plan в edit mode. */
  onSave(): void {
    if (
      this.hasPlanValidationError
      || this.locked
      || !this.selectedMarketDataCaptureProcessCode
      || !this.selectedInstrumentCode
      || !this.hasPlanChanges
    ) {
      return;
    }

    this.locked = true;
    const captureProcessCode = this.selectedMarketDataCaptureProcessCode;
    const instrumentCode = this.selectedInstrumentCode;
    const sources = this.collectSortedSources();

    this.service.replace(captureProcessCode, instrumentCode, { sources }).subscribe({
      next: () => {
        this.snack.open(
          `Plan '${captureProcessCode}' for '${instrumentCode}' replaced`,
          'OK',
          { duration: 2500 }
        );
        this.refreshList();
        this.loadedSourcesFingerprint = this.sourcesFingerprint(sources);
        this.locked = false;
      },
      error: err => {
        this.unlockAfterError(err, 'Replace failed');
      }
    });
  }

  /* Удалить выбранный план из editor-а. */
  onDeleteSelected(): void {
    if (!this.selectedMarketDataCaptureProcessCode || !this.selectedInstrumentCode || this.locked) {
      return;
    }

    this.deletePlan(this.selectedMarketDataCaptureProcessCode, this.selectedInstrumentCode, true);
  }

  /* Удалить план из нижнего списка. */
  onDeleteFromList(plan: MarketDataSourcePlanResponseDto): void {
    const isCurrentPlan = this.selectedMarketDataCaptureProcessCode === plan.captureProcessCode
      && this.selectedInstrumentCode === plan.instrumentCode;
    this.deletePlan(plan.captureProcessCode, plan.instrumentCode, isCurrentPlan);
  }

  /* Сбросить форму и вернуться в create mode. */
  onReset(): void {
    this.editing = false;
    this.selectedMarketDataCaptureProcessCode = null;
    this.selectedInstrumentCode = null;
    this.loadedSourcesFingerprint = null;
    this.locked = false;

    this.form.reset({ captureProcessCode: '', instrumentCode: '' });
    this.form.controls['captureProcessCode'].enable();
    this.form.controls['instrumentCode'].enable();

    this.sources.clear();
    this.onAddSourceRow();
  }

  /* Отменить режим редактирования. */
  cancelEdit(): void {
    this.onReset();
  }

  /* Получить first provider для обзорной таблицы. */
  retiredCount(plan: MarketDataSourcePlanResponseDto): number {
    return plan.sources.filter(source => this.isRetiredSource(source)).length;
  }

  firstActiveProvider(plan: MarketDataSourcePlanResponseDto): string {
    const sorted = plan.sources
      .filter(source => !this.isRetiredSource(source))
      .sort((a, b) => a.priority - b.priority);

    return sorted[0]?.providerCode ?? '-';
  }

  isRetiredSource(source: Pick<MarketDataSourceResponseDto, 'lifecycleStatus'>): boolean {
    return source.lifecycleStatus === 'RETIRED';
  }

  hasRetiredSources(plan: MarketDataSourcePlanResponseDto): boolean {
    return this.retiredCount(plan) > 0;
  }

  hasOnlyRetiredSources(plan: MarketDataSourcePlanResponseDto): boolean {
    return plan.sources.length > 0 && this.retiredCount(plan) === plan.sources.length;
  }

  /* Привести source-строки к DTO и отсортировать по priority перед отправкой. */
  private collectSortedSources(): MarketDataSourceRequestDto[] {
    return this.sources.getRawValue()
      .map((row: { providerCode: string; priority: number }) => ({
        providerCode: row.providerCode,
        priority: Number(row.priority)
      }))
      .sort((a, b) => a.priority - b.priority);
  }

  private sourcesFingerprint(
    sources: Array<{ providerCode: string; priority: number }>
  ): string {
    return [...sources]
      .map(source => ({
        providerCode: source.providerCode,
        priority: Number(source.priority)
      }))
      .sort((left, right) => {
        const priorityCompare = left.priority - right.priority;

        if (priorityCompare !== 0) {
          return priorityCompare;
        }

        return left.providerCode.localeCompare(right.providerCode);
      })
      .map(source => `${source.priority}:${source.providerCode}`)
      .join('|');
  }

  /* Унифицированное удаление плана (из editor или из списка). */
  private deletePlan(
    captureProcessCode: string,
    instrumentCode: string,
    resetEditor: boolean
  ): void {
    this.service.delete(captureProcessCode, instrumentCode).subscribe({
      next: () => {
        this.snack.open(
          `Plan '${captureProcessCode}' for '${instrumentCode}' deleted`,
          'OK',
          { duration: 2500 }
        );
        this.refreshList();

        if (resetEditor) {
          this.onReset();
        }
      },
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Delete failed'), 'Close');
      }
    });
  }

  /* Единая обработка ошибки с разблокировкой действий после закрытия уведомления. */
  private unlockAfterError(err: unknown, fallback: string): void {
    const ref = this.snack.open(this.resolveErrorMessage(err, fallback), 'Close', { duration: 0 });
    ref.afterDismissed().subscribe(() => {
      this.locked = false;
    });
  }

  /* Единая утилита извлечения текста ошибки из ProblemDetail и стандартного HttpErrorResponse. */
  private resolveErrorMessage(err: unknown, fallback: string): string {
    if (!err || typeof err !== 'object') {
      return fallback;
    }

    const body = (err as { error?: unknown }).error;
    if (body && typeof body === 'object') {
      const detail = (body as { detail?: unknown }).detail;
      if (typeof detail === 'string' && detail.trim().length > 0) {
        return detail;
      }

      const title = (body as { title?: unknown }).title;
      if (typeof title === 'string' && title.trim().length > 0) {
        return title;
      }

      const message = (body as { message?: unknown }).message;
      if (typeof message === 'string' && message.trim().length > 0) {
        return message;
      }
    }

    const message = (err as { message?: unknown }).message;
    if (typeof message === 'string' && message.trim().length > 0) {
      return message;
    }

    return fallback;
  }
}
