import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatTableDataSource, MatTableModule} from '@angular/material/table';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {MatCardModule} from '@angular/material/card';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {SettingsDto} from '../../models/settings.model';
import {SettingsCreateDto} from '../../models/settings-create.model';
import {SettingsUpdateDto} from '../../models/settings-update.model';
import {SettingsService} from '../../services/settings.service';
import {PairDto} from '../../../../../pairs/models/pair.model';
import {PairService} from '../../../../../pairs/services/pair.service';
import {ProviderDto} from '../../../../../providers/models/provider.model';
import {ProviderService} from '../../../../../providers/services/provider.service';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-settings-admin',
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
    MatCheckboxModule,
    RouterLink
  ],
  templateUrl: './settings-admin.component.html',
  styleUrl: './settings-admin.component.scss'
})
export class SettingsAdminComponent implements OnInit {

  //=================
  // Табличные данные
  //=================
  displayed: string[] = ['pair', 'provider', 'mode', 'priority', 'refreshMs', 'enabled', 'actions'];
  dataSource = new MatTableDataSource<SettingsDto>([]);

  //========================
  // Форма добавления настроек
  //========================
  form: FormGroup;

  locked = false; // блокировка кнопки
  editing = false;
  editPair: string | null = null;
  editProvider: string | null = null;
  editMode: string | null = null;
  pairs: PairDto[] = [];
  providers: { name: string, modes: string[] }[] = [];
  availableModes: string[] = ['PULL', 'PUSH'];

  constructor(
    private readonly service: SettingsService,
    private readonly pairService: PairService,
    private readonly providerService: ProviderService,
    private readonly fb: FormBuilder,
    private readonly snack: MatSnackBar
  ) {
    this.form = this.fb.group({
      pair: ['', [Validators.required]],
      provider: ['', [Validators.required, Validators.maxLength(50)]],
      mode: ['PULL', [Validators.required]],
      priority: [1, [Validators.required, Validators.min(0), Validators.max(32767)]],
      refreshMs: [1000, [Validators.required, Validators.min(0)]],
      enabled: [true]
    });

    // реакция на выбор режима
    this.form.controls['mode'].valueChanges
      .subscribe(m => this.updateRefreshCtrl(m));

    // реакция на выбор провайдера
    this.form.controls['provider'].valueChanges
      .subscribe(p => this.updateModeCtrl(p));

    // начальное состояние поля периода
    this.updateRefreshCtrl(this.form.controls['mode'].value);
  }

  //=======================
  // Активность на странице
  //=======================
  ngOnInit(): void {
    this.refresh();
    this.pairService.list().subscribe({
      next: p => this.pairs = p,
      error: err => this.snack.open(err.message ?? 'Load pairs failed', 'Close')
    });
    this.providerService.list().subscribe({
      next: list => {
        const map = new Map<string, string[]>();
        list.forEach(pr => {
          const arr = map.get(pr.name) ?? [];
          if (!arr.includes(pr.mode)) { arr.push(pr.mode); }
          map.set(pr.name, arr);
        });
        this.providers = Array.from(map.entries())
          .map(([name, modes]) => ({ name, modes }));
        this.updateModeCtrl(this.form.controls['provider'].value);
      },
      error: err => this.snack.open(err.message ?? 'Load providers failed', 'Close')
    });
  }

  onAdd(): void {
    if (this.form.invalid || this.locked) { return; }

    this.locked = true;

    const dto: SettingsCreateDto = this.form.value;

    this.service.add(dto).subscribe({
      next: id => {
        this.snack.open(`Settings '${id}' added`, 'OK', { duration: 2500 });
        this.refresh();
        this.form.reset({ mode: 'PULL', priority: 1, refreshMs: 1000, enabled: true, pair: '', provider: '' });
        this.form.controls['refreshMs'].enable();
        this.updateModeCtrl(this.form.controls['provider'].value);
        this.updateRefreshCtrl(this.form.controls['mode'].value);
        this.locked = false;
      },
      error: err => {
        const msg = err.error?.message ?? err.message ?? 'Add failed';
        const ref = this.snack.open(msg, 'Close', { duration: 0 });
        ref.afterDismissed().subscribe(() => this.locked = false);
      }
    });
  }

  onEdit(c: SettingsDto): void {
    this.editing = true;
    this.editPair = c.pair;
    this.editProvider = c.provider;
    this.editMode = c.mode;
    this.form.setValue({
      pair: c.pair,
      provider: c.provider,
      mode: c.mode,
      priority: c.priority,
      refreshMs: c.refreshMs,
      enabled: c.enabled
    });
    this.updateModeCtrl(c.provider);
    this.updateRefreshCtrl(c.mode);
    this.form.controls['pair'].disable();
    this.form.controls['provider'].disable();
    this.form.controls['mode'].disable();
  }

  onSave(): void {
    if (this.form.invalid || this.locked || !this.editPair || !this.editProvider || !this.editMode) { return; }

    this.locked = true;

    const dto: SettingsUpdateDto = {
      priority: this.form.controls['priority'].value,
      refreshMs: this.form.controls['refreshMs'].value,
      enabled: this.form.controls['enabled'].value
    } as SettingsUpdateDto;

    this.service.update(this.editPair, this.editProvider, this.editMode, dto).subscribe({
      next: () => {
        this.snack.open(`Settings '${this.editPair}:${this.editProvider}' updated`, 'OK', { duration: 2500 });
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

  cancelEdit(): void {
    this.editing = false;
    this.editPair = null;
    this.editProvider = null;
    this.editMode = null;
    this.form.reset({ pair: '', provider: '', mode: 'PULL', priority: 1, refreshMs: 1000, enabled: true });
    this.form.controls['pair'].enable();
    this.form.controls['provider'].enable();
    this.form.controls['mode'].enable();
    this.form.controls['refreshMs'].enable();
    this.updateModeCtrl(this.form.controls['provider'].value);
    this.updateRefreshCtrl(this.form.controls['mode'].value);
    this.locked = false;
  }

  /**
   * Обновляем состояние поля режима в зависимости от провайдера
   */
  private updateModeCtrl(provider: string): void {
    const modeCtrl = this.form.controls['mode'];
    const item = this.providers.find(p => p.name === provider);
    if (!item) { this.availableModes = ['PULL', 'PUSH']; modeCtrl.enable(); return; }

    this.availableModes = item.modes;

    if (item.modes.length === 1) {
      modeCtrl.setValue(item.modes[0]);
      modeCtrl.disable();
    } else {
      modeCtrl.enable();
      if (!item.modes.includes(modeCtrl.value)) {
        modeCtrl.setValue(item.modes[0]);
      }
    }
    this.updateRefreshCtrl(modeCtrl.value);
  }

  /**
   * Обновляем состояние поля периода в зависимости от режима
   */
  private updateRefreshCtrl(mode: string): void {
    const ctrl = this.form.controls['refreshMs'];
    if (mode === 'PUSH') {
      ctrl.setValue(0);
      ctrl.disable();
    } else {
      ctrl.enable();
      if (ctrl.value === 0) {
        ctrl.setValue(1000);
      }
    }
  }

  onDelete(pair: string, provider: string, mode: string): void {
    this.service.delete(pair, provider, mode).subscribe({
      next: () => {
        this.snack.open(`Settings '${pair}:${provider}' deleted`, 'OK', { duration: 2500 });
        this.refresh();
      },
      error: err => this.snack.open(err.error?.message ?? err.message ?? 'Delete failed', 'Close')
    });
  }

  private refresh(): void {
    this.service.list().subscribe({
      next: list => this.dataSource.data = list,
      error: err => this.snack.open(err.message ?? 'Load failed', 'Close')
    });
  }
}
