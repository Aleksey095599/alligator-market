import { Component, OnDestroy, OnInit } from '@angular/core';
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
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';

import {
  SourcePlanResponseDto,
  SourceResponseDto
} from '../../models/source-plan.model';
import {
  CapturerOptionDto,
  InstrumentOptionDto,
  SourceOptionDto
} from '../../models/source-plan-options.model';
import { SourceRequestDto } from '../../models/create-source-plan.model';
import { SourcePlanService } from '../../services/source-plan.service';

/* Админ-страница управления whole-plan sourcing планами по инструментам. */
@Component({
  selector: 'app-source-plan-admin',
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
  templateUrl: './source-plan-admin.component.html',
  styleUrl: './source-plan-admin.component.scss'
})
export class SourcePlanAdminComponent implements OnInit, OnDestroy {

  private static readonly notBlank = (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (typeof value !== 'string') {
      return null;
    }

    return value.trim().length === 0 ? { blank: true } : null;
  };

  /* Колонки таблицы с существующими планами. */
  displayed: string[] = [
    'planStatus',
    'capturerCode',
    'instrumentCode',
    'sourceCount',
    'firstAvailableSource',
    'actions'
  ];
  dataSource = new MatTableDataSource<SourcePlanResponseDto>([]);
  private allPlans: SourcePlanResponseDto[] = [];
  navigationCapturerCode: string | null = null;
  navigationInstrumentCode: string | null = null;
  private readonly routeSubscription = new Subscription();

  /* Опции для option формы. */
  private registeredCapturerOptions: CapturerOptionDto[] = [];
  private registeredCapturerCodes = new Set<string>();
  capturerOptions: CapturerOptionDto[] = [];
  instruments: InstrumentOptionDto[] = [];
  private availableSourceOptions: SourceOptionDto[] = [];
  private availableSourceCodes = new Set<string>();
  sourceOptions: SourceOptionDto[] = [];

  /* Флаг блокировки кнопок при запросе. */
  locked = false;
  /* Режим редактора: create/edit. */
  editing = false;
  /* Текущий выбранный план в режиме редактирования. */
  selectedCapturerCode: string | null = null;
  selectedInstrumentCode: string | null = null;
  private loadedSourcesFingerprint: string | null = null;

  /* Главная форма whole-plan редактора. */
  form: FormGroup;

  constructor(
    private readonly service: SourcePlanService,
    private readonly fb: FormBuilder,
    private readonly snack: MatSnackBar,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) {
    this.form = this.fb.group({
      capturerCode: ['', [Validators.required, SourcePlanAdminComponent.notBlank]],
      instrumentCode: ['', [Validators.required]],
      sources: this.fb.array([])
    });
  }

  /* Загрузка данных при открытии страницы. */
  ngOnInit(): void {
    this.routeSubscription.add(
      this.route.queryParamMap.subscribe(queryParams => {
        this.navigationCapturerCode = this.normalizedQueryParam(queryParams.get('capturerCode'));
        this.navigationInstrumentCode = this.normalizedQueryParam(queryParams.get('instrumentCode'));
        this.applyNavigationFilter();
        this.openNavigationPlanIfExact();
      })
    );

    this.refreshList();
    this.loadOptions();
    this.onAddSourceRow();
  }

  ngOnDestroy(): void {
    this.routeSubscription.unsubscribe();
  }

  /* Удобный доступ к FormArray sources. */
  get sources(): FormArray {
    return this.form.controls['sources'] as FormArray;
  }

  /* Быстрый список source-контролов для шаблона. */
  get sourceControls(): AbstractControl[] {
    return this.sources.controls;
  }

  /* Есть ли дубликаты источников в редакторе. */
  get hasDuplicateSources(): boolean {
    const values = this.sourceControls
      .map(c => c.get('sourceCode')?.value)
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
      || this.hasRetiredSourceRows
      || this.hasDuplicateSources
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
        this.allPlans = plans;
        this.applyNavigationFilter();
        this.openNavigationPlanIfExact();
      },
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Load source plans failed'), 'Close');
      }
    });
  }

  get hasNavigationFilter(): boolean {
    return this.navigationCapturerCode !== null || this.navigationInstrumentCode !== null;
  }

  clearNavigationFilter(): void {
    void this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {},
      replaceUrl: true
    });
  }

  /* Загрузить опции для option editor-а. */
  loadOptions(): void {
    this.service.getOptions().subscribe({
      next: options => {
        this.registeredCapturerOptions = options.capturers;
        this.registeredCapturerCodes = new Set(options.capturers.map(capturer => capturer.code));
        this.syncCapturerOptionsForCurrentMode();

        this.instruments = options.instruments;
        this.availableSourceOptions = options.sources;
        this.availableSourceCodes = new Set(options.sources.map(source => source.code));
        this.syncSourceOptionsForCurrentMode();
      },
      error: err => {
        this.snack.open(this.resolveErrorMessage(err, 'Load options failed'), 'Close');
      }
    });
  }

  /* Добавить новую строку source в FormArray. */
  onAddSourceRow(source?: SourceResponseDto): void {
    this.sources.push(this.fb.group({
      sourceCode: [source?.sourceCode ?? '', [Validators.required]],
      priority: [source?.priority ?? 0, [Validators.required, Validators.min(0)]],
      lifecycleStatus: [source?.lifecycleStatus ?? 'AVAILABLE']
    }));
  }

  /* Удалить строку source по индексу. */
  onRemoveSourceRow(index: number): void {
    if (this.sources.length <= 1) {
      return;
    }

    this.sources.removeAt(index);
    this.syncSourceOptionsForCurrentMode();
  }

  /* Открыть план в editor и перейти в edit mode. */
  onEditPlan(plan: SourcePlanResponseDto): void {
    this.service.get(plan.capturerCode, plan.instrumentCode).subscribe({
      next: fullPlan => {
        this.editing = true;
        this.selectedCapturerCode = fullPlan.capturerCode;
        this.selectedInstrumentCode = fullPlan.instrumentCode;

        this.syncCapturerOptionsForCurrentMode();
        this.form.controls['capturerCode'].setValue(fullPlan.capturerCode);
        this.form.controls['capturerCode'].disable();
        this.form.controls['instrumentCode'].setValue(fullPlan.instrumentCode);
        this.form.controls['instrumentCode'].disable();

        this.sources.clear();
        this.sourceOptions = this.sourceOptionsWithHistoricalCodes(
          fullPlan.sources.map(source => source.sourceCode)
        );
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

    const capturerCode = this.form.controls['capturerCode'].value;
    const instrumentCode = this.form.controls['instrumentCode'].value;
    const sources = this.collectSortedSources();

    this.service.create({ capturerCode, instrumentCode, sources }).subscribe({
      next: () => {
        this.snack.open(
          `Plan '${capturerCode}' for '${instrumentCode}' created`,
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
      || !this.selectedCapturerCode
      || !this.selectedInstrumentCode
      || !this.hasPlanChanges
    ) {
      return;
    }

    this.locked = true;
    const capturerCode = this.selectedCapturerCode;
    const instrumentCode = this.selectedInstrumentCode;
    const sources = this.collectSortedSources();

    this.service.replace(capturerCode, instrumentCode, { sources }).subscribe({
      next: () => {
        this.snack.open(
          `Plan '${capturerCode}' for '${instrumentCode}' replaced`,
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
    if (!this.selectedCapturerCode || !this.selectedInstrumentCode || this.locked) {
      return;
    }

    this.deletePlan(this.selectedCapturerCode, this.selectedInstrumentCode, true);
  }

  /* Удалить план из нижнего списка. */
  onDeleteFromList(plan: SourcePlanResponseDto): void {
    const isCurrentPlan = this.selectedCapturerCode === plan.capturerCode
      && this.selectedInstrumentCode === plan.instrumentCode;
    this.deletePlan(plan.capturerCode, plan.instrumentCode, isCurrentPlan);
  }

  /* Сбросить форму и вернуться в create mode. */
  onReset(): void {
    this.editing = false;
    this.selectedCapturerCode = null;
    this.selectedInstrumentCode = null;
    this.loadedSourcesFingerprint = null;
    this.locked = false;
    this.capturerOptions = this.registeredCapturerOptions;

    this.form.reset({ capturerCode: '', instrumentCode: '' });
    this.form.controls['capturerCode'].enable();
    this.form.controls['instrumentCode'].enable();

    this.sources.clear();
    this.sourceOptions = this.availableSourceOptions;
    this.onAddSourceRow();
  }

  /* Отменить режим редактирования. */
  cancelEdit(): void {
    this.onReset();
  }

  /* Counts source rows that are unavailable because their source is retired. */
  retiredCount(plan: SourcePlanResponseDto): number {
    return plan.sources.filter(source => this.isRetiredSource(source)).length;
  }

  firstAvailableSource(plan: SourcePlanResponseDto): string {
    const sorted = plan.sources
      .filter(source => !this.isRetiredSource(source))
      .sort((a, b) => a.priority - b.priority);

    return sorted[0]?.sourceCode ?? '-';
  }

  isRetiredSource(source: Pick<SourceResponseDto, 'lifecycleStatus'>): boolean {
    return source.lifecycleStatus === 'SOURCE_RETIRED';
  }

  sourceStatusLabel(source: Pick<SourceResponseDto, 'lifecycleStatus'>): string {
    return this.isRetiredSource(source) ? 'SOURCE RETIRED' : 'AVAILABLE';
  }

  isAvailablePlan(plan: Pick<SourcePlanResponseDto, 'planExecutionStatus'>): boolean {
    return plan.planExecutionStatus === 'AVAILABLE';
  }

  isBlockedPlan(plan: Pick<SourcePlanResponseDto, 'planExecutionStatus'>): boolean {
    return !this.isAvailablePlan(plan);
  }

  planStatusLabel(plan: Pick<SourcePlanResponseDto, 'planExecutionStatus'>): string {
    switch (plan.planExecutionStatus) {
      case 'AVAILABLE':
        return 'AVAILABLE';
      case 'CAPTURER_RETIRED':
        return 'CAPTURER RETIRED';
      case 'NO_AVAILABLE_SOURCES':
        return 'NO AVAILABLE SOURCES';
    }
  }

  get hasRetiredSourceRows(): boolean {
    return this.sourceControls.some(control => this.isRetiredSource(control.value));
  }

  isSourceAvailable(sourceCode: string): boolean {
    return this.availableSourceCodes.has(sourceCode);
  }

  isCapturerRegistered(capturerCode: string): boolean {
    return this.registeredCapturerCodes.has(capturerCode);
  }

  hasRetiredSources(plan: SourcePlanResponseDto): boolean {
    return this.retiredCount(plan) > 0;
  }

  hasOnlyRetiredSources(plan: SourcePlanResponseDto): boolean {
    return plan.sources.length > 0 && this.retiredCount(plan) === plan.sources.length;
  }

  private applyNavigationFilter(): void {
    this.dataSource.data = this.allPlans.filter(plan => this.matchesNavigationFilter(plan));
  }

  private matchesNavigationFilter(plan: SourcePlanResponseDto): boolean {
    return (this.navigationCapturerCode === null || plan.capturerCode === this.navigationCapturerCode)
      && (this.navigationInstrumentCode === null || plan.instrumentCode === this.navigationInstrumentCode);
  }

  private openNavigationPlanIfExact(): void {
    if (!this.navigationCapturerCode || !this.navigationInstrumentCode) {
      return;
    }

    if (
      this.selectedCapturerCode === this.navigationCapturerCode
      && this.selectedInstrumentCode === this.navigationInstrumentCode
    ) {
      return;
    }

    const plan = this.allPlans.find(candidate =>
      candidate.capturerCode === this.navigationCapturerCode
      && candidate.instrumentCode === this.navigationInstrumentCode
    );

    if (plan) {
      this.onEditPlan(plan);
    }
  }

  private normalizedQueryParam(value: string | null): string | null {
    const normalized = value?.trim();
    return normalized ? normalized : null;
  }

  /* Привести source-строки к DTO и отсортировать по priority перед отправкой. */
  private syncCapturerOptionsForCurrentMode(): void {
    if (!this.editing || !this.selectedCapturerCode) {
      this.capturerOptions = this.registeredCapturerOptions;
      return;
    }

    this.capturerOptions = this.capturerOptionsWithHistoricalCode(
      this.selectedCapturerCode
    );
  }

  /* Retired capturer code добавляется только для отображения уже загруженного плана. */
  private capturerOptionsWithHistoricalCode(
    capturerCode: string
  ): CapturerOptionDto[] {
    if (this.registeredCapturerCodes.has(capturerCode)) {
      return this.registeredCapturerOptions;
    }

    return [
      {
        code: capturerCode,
        displayName: capturerCode
      },
      ...this.registeredCapturerOptions
    ];
  }

  private syncSourceOptionsForCurrentMode(): void {
    if (!this.editing) {
      this.sourceOptions = this.availableSourceOptions;
      return;
    }

    const sourceCodes = this.sources.getRawValue()
      .map((row: { sourceCode?: string }) => row.sourceCode)
      .filter((sourceCode: string | undefined): sourceCode is string => !!sourceCode);

    this.sourceOptions = this.sourceOptionsWithHistoricalCodes(sourceCodes);
  }

  /* Retired source codes добавляются только для отображения уже загруженных строк в edit mode. */
  private sourceOptionsWithHistoricalCodes(sourceCodes: string[]): SourceOptionDto[] {
    const historicalCodes = Array.from(new Set(sourceCodes))
      .filter(sourceCode => !this.availableSourceCodes.has(sourceCode))
      .map(code => ({ code }));

    return [...historicalCodes, ...this.availableSourceOptions];
  }

  private collectSortedSources(): SourceRequestDto[] {
    return this.sources.getRawValue()
      .map((row: { sourceCode: string; priority: number }) => ({
        sourceCode: row.sourceCode,
        priority: Number(row.priority)
      }))
      .sort((a, b) => a.priority - b.priority);
  }

  private sourcesFingerprint(
    sources: Array<{ sourceCode: string; priority: number }>
  ): string {
    return [...sources]
      .map(source => ({
        sourceCode: source.sourceCode,
        priority: Number(source.priority)
      }))
      .sort((left, right) => {
        const priorityCompare = left.priority - right.priority;

        if (priorityCompare !== 0) {
          return priorityCompare;
        }

        return left.sourceCode.localeCompare(right.sourceCode);
      })
      .map(source => `${source.priority}:${source.sourceCode}`)
      .join('|');
  }

  /* Унифицированное удаление плана (из editor или из списка). */
  private deletePlan(
    capturerCode: string,
    instrumentCode: string,
    resetEditor: boolean
  ): void {
    this.service.delete(capturerCode, instrumentCode).subscribe({
      next: () => {
        this.snack.open(
          `Plan '${capturerCode}' for '${instrumentCode}' deleted`,
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
